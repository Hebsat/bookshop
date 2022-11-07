package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.BookFile;
import com.example.MyBookShopApp.errors.WrongEntityException;
import com.example.MyBookShopApp.repositories.BookFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

@Service
public class ResourceStorageService {

    @Value("${bookshop.resources.root}")
    private String rootPath;
    @Value("${bookshop.resources.books}")
    private String booksPath;
    @Value("${bookshop.resources.authors}")
    private String authorsPath;
    @Value("${bookshop.resources.book-files}")
    private String bookFilesPath;

    private final BookService bookService;
    private final AuthorService authorService;
    private final BookFileRepository bookFileRepository;

    @Autowired
    public ResourceStorageService(BookService bookService, AuthorService authorService, BookFileRepository bookFileRepository) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.bookFileRepository = bookFileRepository;
    }

    private String saveNewData(MultipartFile file, String name, Path path) throws IOException {
        String fileName = null;
        if (Files.notExists(path)) {
            Files.createDirectories(path);
            Logger.getLogger(this.getClass().getName()).info("Created file directory in " + path);
        }
        if (!file.isEmpty()) {
            fileName = name + '.' + FilenameUtils.getExtension(file.getOriginalFilename());
            path = path.resolve(fileName);
            file.transferTo(path);
            Logger.getLogger(this.getClass().getName()).info(fileName + " uploaded successful!");
        }
        return fileName;
    }

    public void saveNewBookCoverImage(MultipartFile file, String slug) {
        String filePath = "/data" + booksPath + "/";
        try {
            filePath += saveNewData(file, slug, Path.of(rootPath, booksPath));
            Book book = bookService.getBookBySlug(slug);
            book.setImage(filePath);
            bookService.saveBook(book);
        } catch (IOException | WrongEntityException e) {
            Logger.getLogger(this.getClass().getName()).info("saveNewBookCoverImage - " + e.getMessage());
        }
    }

    public void saveNewAuthorPhoto(MultipartFile file, String slug) {
        String filePath = "/data" + authorsPath + "/";
        try {
            filePath += saveNewData(file, slug, Path.of(rootPath, authorsPath));
            Author author = authorService.getAuthorBySlug(slug);
            author.setPhoto(filePath);
            authorService.saveAuthor(author);
        } catch (IOException | WrongEntityException e) {
            Logger.getLogger(this.getClass().getName()).info("saveNewAuthorPhoto - " + e.getMessage());
        }
    }

    public BookFile getBookFileByHash(String hash) {
        Logger.getLogger(this.getClass().getName()).info("getBookFileByHash with hash: " + hash);
        return bookFileRepository.findBookFileByHash(hash);
    }

    public Path getBookFilePath(BookFile bookFile) {
        return Path.of(bookFile.getPath());
    }

    public MediaType getBookFileMime(BookFile bookFile) {
        String mimeType = URLConnection.guessContentTypeFromName(Path.of(bookFile.getPath()).getFileName().toString());
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    public byte[] getBookFileByteArray(BookFile bookFile) {
        Path path = Path.of(rootPath, bookFilesPath, bookFile.getPath());
        byte[] fileBytes = null;
        try {
            fileBytes = Files.readAllBytes(path);
        }
        catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).info("getBookFileByteArray - " + e.getMessage());
        }
        Logger.getLogger(this.getClass().getName()).info("file bytes wrote successfully");
        return fileBytes;
    }
}
