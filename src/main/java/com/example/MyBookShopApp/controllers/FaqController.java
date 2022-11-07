package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.services.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class FaqController {

    private final CookieService cookieService;


    @Autowired
    public FaqController(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "main";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "faq";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "It's over 9000  книг в магазине Bookshop!";
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

    @GetMapping("/faq")
    public String searchPage() {
        return "faq";
    }
}
