package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.BookFile;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.errors.WrongResultException;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.BookFileRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ResourceStorageService {

    @Value("${bookshop.resources.root}")
    private String rootPath;
    @Value("${bookshop.resources.books}")
    private String booksPath;
    @Value("${bookshop.resources.authors}")
    private String authorsPath;
    @Value("${bookshop.resources.book-files}")
    private String bookFilesPath;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookFileRepository bookFileRepository;

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

    public ApiSimpleResponse saveNewBookCoverImage(MultipartFile file, String slug) {
        String filePath = "/data" + booksPath + "/";
        try {
            filePath += saveNewData(file, slug, Path.of(rootPath, booksPath));
            Book book = bookRepository.findBySlug(slug).orElseThrow(() -> new WrongResultException("Такой книги не существует!"));
            book.setImage(filePath);
            bookRepository.save(book);
        } catch (IOException | WrongResultException e) {
            Logger.getLogger(this.getClass().getName()).info("saveNewBookCoverImage - " + e.getMessage());
        }
        return new ApiSimpleResponse(true);
    }

    public ApiSimpleResponse saveNewAuthorPhoto(MultipartFile file, String slug) {
        String filePath = "/data" + authorsPath + "/";
        try {
            filePath += saveNewData(file, slug, Path.of(rootPath, authorsPath));
            Author author = authorRepository.findAuthorBySlug(slug).orElseThrow(() -> new WrongResultException("Такого автора не существует!"));
            author.setPhoto(filePath);
            authorRepository.save(author);
        } catch (IOException | WrongResultException e) {
            Logger.getLogger(this.getClass().getName()).info("saveNewAuthorPhoto - " + e.getMessage());
        }
        return new ApiSimpleResponse(true);
    }

    public ResponseEntity<ByteArrayResource> getBookFile(String hash) throws BookshopWrongParameterException {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash)
                .orElseThrow(() -> new BookshopWrongParameterException("Файла с указанным хешем " + hash + " не существует!"));
        Path path = Path.of(bookFile.getPath());
        MediaType mediaType = getBookFileMime(bookFile);
        byte[] data = getBookFileByteArray(bookFile);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    private MediaType getBookFileMime(BookFile bookFile) {
        String mimeType = URLConnection.guessContentTypeFromName(Path.of(bookFile.getPath()).getFileName().toString());
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private byte[] getBookFileByteArray(BookFile bookFile) {
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
