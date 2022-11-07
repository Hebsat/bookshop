package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.BookFile;
import com.example.MyBookShopApp.errors.WrongEntityException;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.ResourceStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final ResourceStorageService resourceStorageService;
    private final CookieService cookieService;

    @Autowired
    public BooksController(BookService bookService, AuthorService authorService, ResourceStorageService resourceStorage, CookieService cookieService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.resourceStorageService = resourceStorage;
        this.cookieService = cookieService;
    }

    @ModelAttribute("searchQueryDto")
    public SearchQueryDto searchQueryDto() {
        return new SearchQueryDto();
    }

    @ModelAttribute("booksInCart")
    public int booksInCart(@CookieValue(name = "cartContents", required = false) String contents) {
        return cookieService.getCountOfBooksInCookie(contents);
    }

    @ModelAttribute("booksInPostponed")
    public int booksInPostponed(@CookieValue(name = "postponedContents", required = false) String contents) {
        return cookieService.getCountOfBooksInCookie(contents);
    }

    @GetMapping("/recent")
    public String recentBooksPage(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false, defaultValue = "${bookshop.default.page}") int offset,
            @RequestParam(required = false, defaultValue = "${bookshop.default.size}") int limit,
            Model model) {
        Page<Book> bookPage;
        if (from != null && to != null) {
            bookPage = bookService.getPageOfRecentBooks(from, offset, limit, to);
        } else {
            bookPage = bookService.getPageOfRecentBooks(offset, limit);
        }
        if (bookPage.getTotalPages() == 1) {
            model.addAttribute("lastPage", true);
        }
        model.addAttribute("recentBooks", bookPage.getContent());
        model.addAttribute("topBarIdentifier", "recent");
        model.addAttribute("pageTitle", "recent");
        model.addAttribute("pageHeadDescription", "It's over 9000 книг...");
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularBooksPage(Model model) {
        List<Book> bookList = bookService.getPageOfPopularBooks().getContent();
        model.addAttribute("popularBooks", bookList);
        model.addAttribute("topBarIdentifier", "popular");
        model.addAttribute("pageTitle", "popular");
        model.addAttribute("pageHeadDescription", "It's over 9000 книг...");
        return "books/popular";
    }

    @GetMapping("/{slug}")
    public String getBook(@PathVariable String slug, Model model) throws WrongEntityException {
        Book book = bookService.getBookBySlug(slug);
        model.addAttribute("topBarIdentifier", "genres");
        model.addAttribute("pageTitle", "book");
        model.addAttribute("pageTitlePart", book.getTitle());
        model.addAttribute("pageHeadDescription", book.getTitle() + " Описание: " + book.getDescription().substring(0, 100) + "...");
        model.addAttribute("book", book);
        return "/books/slug";
    }

    @GetMapping("/author/{slug}")
    public String getAuthorBooks(@PathVariable String slug, Model model) throws WrongEntityException {
        Author author = authorService.getAuthorBySlug(slug);
        Page<Book> bookPage = authorService.getPageOfBooksByAuthor(author);
        if (bookPage.getTotalPages() == 1) {
            model.addAttribute("lastPage", true);
        }
        model.addAttribute("topBarIdentifier", "genres");
        model.addAttribute("pageTitle", "books");
        model.addAttribute("pageTitlePart", author.getName());
        model.addAttribute("pageHeadDescription",
                "Книги автора " + author.getName() + ": " + Arrays.toString(author.getBookList().stream().map(Book::getTitle).toArray()));
        model.addAttribute("author", author);
        model.addAttribute("books", bookPage.getContent());
        return "/books/author";
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksListDto recommendedBooksPage(
            @RequestParam int offset,
            @RequestParam int limit) {
        return new BooksListDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/recent/page")
    @ResponseBody
    public BooksListDto recentBooksPage(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam int offset,
            @RequestParam int limit) {
        System.out.println(from + " " + to);
        if (from != null && to != null) {
            return new BooksListDto(bookService.getPageOfRecentBooks(from, offset, limit, to).getContent());
        } else {
            return new BooksListDto(bookService.getPageOfRecentBooks(offset, limit).getContent());
        }
    }

    @GetMapping("/popular/page")
    @ResponseBody
    public BooksListDto popularBooksPage(
            @RequestParam int offset,
            @RequestParam int limit) {
        return new BooksListDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }

    @GetMapping("/author/{slug}/page")
    @ResponseBody
    public BooksListDto getAuthorBooksPage(
            @PathVariable String slug,
            @RequestParam int offset,
            @RequestParam int limit) throws WrongEntityException {
        Author author = authorService.getAuthorBySlug(slug);
        return new BooksListDto(authorService.getPageOfBooksByAuthor(author, offset, limit).getContent());
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@PathVariable String slug, @RequestParam MultipartFile file) {
        resourceStorageService.saveNewBookCoverImage(file, slug);
        return "redirect:/books/" + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> getBookFile(@PathVariable String hash) {
        BookFile bookFile = resourceStorageService.getBookFileByHash(hash);
        Path path = resourceStorageService.getBookFilePath(bookFile);
        MediaType mediaType = resourceStorageService.getBookFileMime(bookFile);
        byte[] data = resourceStorageService.getBookFileByteArray(bookFile);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString())
                .contentType(mediaType)
//                .contentLength(data.length)
//                .header(HttpHeaders.DATE, LocalDateTime.now().format(DateTimeFormatter
//                        .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
//                        .withZone(ZoneId.of("Europe/Moscow"))))
                .body(new ByteArrayResource(data));
    }
}
