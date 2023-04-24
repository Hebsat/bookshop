package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
@RequiredArgsConstructor
public class ErrorsController {

    private final CookieService cookieService;
    private final RegistrationService registrationService;

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "error";
    }

    @ModelAttribute("error")
    public String error() {
        return "Что-то пошло не так..";
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

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return registrationService.getCurrentUser();
    }

    @GetMapping("/404")
    public String notFound() {
        return "errors/404";
    }
}
