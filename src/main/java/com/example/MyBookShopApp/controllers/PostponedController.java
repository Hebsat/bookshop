package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PostponedController {

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "postponed";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "It's over 9000  книг в магазине Bookshop!";
    }

    @GetMapping("/postponed")
    public String mainPage() {
        return "postponed";
    }
}
