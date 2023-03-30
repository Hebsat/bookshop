package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.ResourceStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;
    private final ResourceStorageService resourceStorageService;
    private final CookieService cookieService;

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
    public String getAuthor(@PathVariable String slug, Model model) {

        return authorService.getAuthorBySlug(slug, model);
    }

    @PostMapping("/{slug}/img/save")
    @ResponseBody
    public ApiSimpleResponse saveNewAuthorImage(@PathVariable String slug, @RequestParam MultipartFile file) {

        return resourceStorageService.saveNewAuthorPhoto(file, slug);
    }
}
