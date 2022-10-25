package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BooksController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @ModelAttribute("searchQueryDto")
    public SearchQueryDto searchQueryDto() {
        return new SearchQueryDto();
    }

    @GetMapping("/recent")
    public String recentBooksPage(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit,
            Model model) {
        List<Book> bookList;
        if (from != null && to != null) {
            bookList = bookService.getPageOfRecentBooks(from, offset, limit, to).getContent();
        } else {
            bookList = bookService.getPageOfRecentBooks().getContent();
        }
        model.addAttribute("recentBooks", bookList);
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
    public String getBook(@PathVariable String slug, Model model) {
        Logger.getLogger(BooksController.class.getName()).info("request book with slug: " + slug);
        Book book = bookService.getBookBySlug(slug);
        Logger.getLogger(BooksController.class.getName()).info(book.toString());
        model.addAttribute("topBarIdentifier", "genres");
        model.addAttribute("pageTitle", "book");
        model.addAttribute("pageTitlePart", book.getTitle());
        model.addAttribute("pageHeadDescription", book.getTitle() + " Описание: " + book.getDescription().substring(0, 100) + "...");
        model.addAttribute("book", bookService.getBookBySlug(slug));
        return "/books/slug";
    }

    @GetMapping("/author/{slug}")
    public String getAuthorBooks(@PathVariable String slug, Model model) {
        Author author = authorService.getAuthorBySlug(slug);
        model.addAttribute("topBarIdentifier", "genres");
        model.addAttribute("pageTitle", "books");
        model.addAttribute("pageTitlePart", author.getName());
        model.addAttribute("pageHeadDescription",
                "Книги автора " + author.getName() + ": " + Arrays.toString(author.getBookList().stream().map(Book::getTitle).toArray()));
        model.addAttribute("author", author);
        model.addAttribute("books", authorService.getPageOfBooksByAuthor(author));
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
            @RequestParam int limit) {
        return new BooksListDto(authorService.getPageOfBooksByAuthor(slug, offset, limit).getContent());
    }
}
