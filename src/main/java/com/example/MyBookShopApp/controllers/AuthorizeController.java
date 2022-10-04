package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthorizeController {

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "bookshop.names.titles.sign_in";
    }

    @GetMapping("/signin")
    public String mainPage() {
        return "signin";
    }
}
