package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CartController {

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "cart";
    }

    @ModelAttribute("pageTitlePart")
    public String pageTitlePart() {
        return "UserName";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "It's over 9000  книг в магазине Bookshop!";
    }

    @GetMapping("/cart")
    public String mainPage() {
        return "cart";
    }
}
