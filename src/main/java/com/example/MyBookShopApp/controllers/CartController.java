package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CartController {

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "bookshop.names.titles.cart";
    }

    @GetMapping("/cart")
    public String mainPage() {
        return "cart";
    }
}
