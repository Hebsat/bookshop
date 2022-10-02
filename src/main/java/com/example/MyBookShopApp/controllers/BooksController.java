package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public String recentBooksPage() {
        return "books/recent";
    }

    @GetMapping("/popular")
    public String popularBooksPage() {
        return "books/popular";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable int id) {
        return "/books/slug";
    }
}
