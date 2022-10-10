package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/recent")
    public String recentBooksPage(Model model) {
        List<Book> bookList = bookService.getRecentBooks();
        model.addAttribute("recentBooks", bookList);
        model.addAttribute("topBarIdentifier", "recent");
        model.addAttribute("pageTitle", "recent");
        model.addAttribute("pageHeadDescription", "It's over 9000 книг...");
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularBooksPage(Model model) {
        List<Book> bookList = bookService.getPopularBooks();
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
        model.addAttribute("pageHeadDescription", "Книги автора " + author.getName() + ": " + Arrays.toString(author.getBookList().stream().map(Book::getTitle).toArray()));
        model.addAttribute("author", author);
        return "/books/author";
    }
}
