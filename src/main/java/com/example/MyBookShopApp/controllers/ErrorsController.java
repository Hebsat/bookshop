package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.services.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorsController {

    private final CookieService cookieService;


    @Autowired
    public ErrorsController(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "main";
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

    @GetMapping("/404")
    public String notFound() {
        return "/errors/404";
    }
}
