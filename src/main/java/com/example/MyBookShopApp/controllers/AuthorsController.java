package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.errors.WrongEntityException;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.ResourceStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Logger;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorService authorService;
    private final ResourceStorageService resourceStorageService;
    private final CookieService cookieService;

    @Autowired
    public AuthorsController(AuthorService authorService, ResourceStorageService resourceStorageService, CookieService cookieService) {
        this.authorService = authorService;
        this.resourceStorageService = resourceStorageService;
        this.cookieService = cookieService;
    }

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "authors";
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

    @GetMapping
    public String authorsPage(Model model) {
        model.addAttribute("pageTitle", "authors");
        model.addAttribute("pageHeadDescription", "It's over 9000 авторов...");
        model.addAttribute("authorsMap", authorService.getAuthorData());
        return "authors/index";
    }

    @GetMapping("/{slug}")
    public String getAuthor(@PathVariable String slug, Model model) throws WrongEntityException {
        Logger.getLogger(AuthorsController.class.getName()).info("request author with slug: " + slug);
        Author author = authorService.getAuthorBySlug(slug);
        model.addAttribute("author", author);
        model.addAttribute("pageTitle", "author");
        model.addAttribute("pageTitlePart", author.getName());
        model.addAttribute("pageHeadDescription",
                author.getName() + ". Биография: " + author.getDescription().substring(0, 100) + "...");
        return "authors/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewAuthorImage(@PathVariable String slug, @RequestParam MultipartFile file) {
        resourceStorageService.saveNewAuthorPhoto(file, slug);
        return "redirect:/authors/" + slug;
    }
}
