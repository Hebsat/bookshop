package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.errors.WrongResultException;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.ResourceStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final ResourceStorageService resourceStorageService;
    private final CookieService cookieService;

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
        BooksListDto booksListDto = bookService.getPageOfRecentBooks(from, offset, limit, to);
        if (booksListDto.getTotalPages() == 1) {
            model.addAttribute("lastPage", true);
        }
        model.addAttribute("recentBooks", booksListDto.getBooks());
        model.addAttribute("topBarIdentifier", "recent");
        model.addAttribute("pageTitle", "recent");
        model.addAttribute("pageHeadDescription", "It's over 9000 книг...");
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularBooksPage(Model model) {
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks().getBooks());
        model.addAttribute("topBarIdentifier", "popular");
        model.addAttribute("pageTitle", "popular");
        model.addAttribute("pageHeadDescription", "It's over 9000 книг...");
        return "books/popular";
    }

    @GetMapping("/{slug}")
    public String getBook(@PathVariable String slug, Model model) {

        return bookService.getBookBySlug(slug, model);
    }

    @GetMapping("/author/{slug}")
    public String getAuthorBooks(@PathVariable String slug, Model model) {

        return authorService.getFirstPageOfBooksByAuthor(slug, model);
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksListDto recommendedBooksPage(
            @RequestParam int offset,
            @RequestParam int limit) {

        return bookService.getPageOfRecommendedBooks(offset, limit);
    }

    @GetMapping("/recent/page")
    @ResponseBody
    public BooksListDto recentBooksPage(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam int offset,
            @RequestParam int limit) {

        return bookService.getPageOfRecentBooks(from, offset, limit, to);
    }

    @GetMapping("/popular/page")
    @ResponseBody
    public BooksListDto popularBooksPage(
            @RequestParam int offset,
            @RequestParam int limit) {

        return bookService.getPageOfPopularBooks(offset, limit);
    }

    @GetMapping("/author/{slug}/page")
    @ResponseBody
    public BooksListDto getAuthorBooksPage(
            @PathVariable String slug,
            @RequestParam int offset,
            @RequestParam int limit) throws BookshopWrongParameterException {

        return authorService.getPageOfBooksByAuthor(slug, offset, limit);
    }

    @PostMapping("/{slug}/img/save")
    @ResponseBody
    public ApiSimpleResponse saveNewBookImage(@PathVariable String slug, @RequestParam MultipartFile file) {

        return resourceStorageService.saveNewBookCoverImage(file, slug);
    }

    @GetMapping("/download/{hash}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getBookFile(@PathVariable String hash) throws BookshopWrongParameterException {

        return resourceStorageService.getBookFile(hash);
    }

    @PostMapping("api/rateBook")
    @ResponseBody
    public ApiSimpleResponse rateBook(@RequestParam String bookId, @RequestParam int value) throws WrongResultException {

        return bookService.rateBook(bookId, value);
    }
}
