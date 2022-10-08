package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ContactsController {

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "main";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "contacts";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "Over 9 000  книг в магазине Bookshop!";
    }

    @GetMapping("/contacts")
    public String searchPage() {
        return "contacts";
    }
}
