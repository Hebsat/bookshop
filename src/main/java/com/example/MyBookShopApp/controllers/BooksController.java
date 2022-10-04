package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    @Autowired
    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        return bookService.getBookData();
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getBookData();
    }

    @GetMapping("/recent")
    public String recentBooksPage(Model model) {
        model.addAttribute("topBarIdentifier", "recent");
        model.addAttribute("pageTitle", "bookshop.names.titles.recent");
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularBooksPage(Model model) {
        model.addAttribute("topBarIdentifier", "popular");
        model.addAttribute("pageTitle", "bookshop.names.titles.popular");
        return "books/popular";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable String id, Model model) {
        Logger.getLogger(AuthorsController.class.getName()).info("request id: " + id);
        bookService.getBook(Integer.parseInt(id));
        model.addAttribute("topBarIdentifier", "genres");
        return "/books/slug";
    }

    @GetMapping("/author/{slug}")
    public String getAuthorBooks(@PathVariable String slug, Model model) {
        model.addAttribute("topBarIdentifier", "genres");
        return "/books/author";
    }
}
