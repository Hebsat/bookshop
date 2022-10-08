package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class SearchController {

    private final BookService bookService;

    @Autowired
    public SearchController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchList")
    public List<Book> searchList() {
        return bookService.getBookData();
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "search";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "Over 9 000  книг в магазине Bookshop!";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "search/index";
    }
}
