package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchQueryDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GenresController {

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "genres";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "genres";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "It's over 9000  книг в магазине Bookshop!";
    }

    @ModelAttribute("searchQueryDto")
    public SearchQueryDto searchQueryDto() {
        return new SearchQueryDto();
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }
}
