package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentsController {

    private final CookieService cookieService;
    private final RegistrationService registrationService;

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "main";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "documents";
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

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return registrationService.getCurrentUser();
    }

    @GetMapping
    public String documentsPage() {
        return "documents/index";
    }

    @GetMapping("/{slug}")
    public String getDocument(@PathVariable String slug, Model model) {
        model.addAttribute("documentName", "Политика обработки персональных данных");
        return "documents/slug";
    }
}
