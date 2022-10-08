package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "authors";
    }

//    @ModelAttribute("pageTitle")
//    public String pageTitle() {
//        return "authors";
//    }

    @GetMapping
    public String authorsPage(Model model) {
        model.addAttribute("pageTitle", "authors");
        model.addAttribute("pageHeadDescription", "Over 9 000 авторов...");
        model.addAttribute("authorsMap", authorService.getAuthorData());
        return "authors/index";
    }

    @GetMapping("/{slug:\\w+[^\\.]+}")
    public String getAuthor(@PathVariable String slug, Model model) {
        Logger.getLogger(AuthorsController.class.getName()).info("request author with slug: " + slug);
        Author author = authorService.getAuthorBySlug(slug);
        model.addAttribute("author", author);
        model.addAttribute("pageTitle", "author");
        model.addAttribute("pageTitlePart", author.getName());
        model.addAttribute("pageHeadDescription", author.getName() + ". Биография: " + author.getDescription().substring(0, 100) + "...");
        return "authors/slug";
    }
}
