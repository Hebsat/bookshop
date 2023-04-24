package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.RegistrationService;
import com.example.MyBookShopApp.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books/tag")
@RequiredArgsConstructor
public class TagsController {

    private final TagService tagService;
    private final CookieService cookieService;
    private final RegistrationService registrationService;

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
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

    @GetMapping("/{slug}")
    public String getTagBooks(@PathVariable String slug, Model model) {

        return tagService.getFirstPageOfBooksByTag(slug, model);
    }

    @GetMapping("/all")
    public String getAllTags(Model model) {
        model.addAttribute("pageTitle", "tags");
        model.addAttribute("tags", tagService.getTags());
        return "tags/all";
    }

    @GetMapping("/{slug}/page")
    @ResponseBody
    public BooksListDto getTagBooksPage(
            @PathVariable String slug,
            @RequestParam int offset,
            @RequestParam int limit) throws BookshopWrongParameterException {

        return tagService.getNextPageOfBooksByTag(slug, offset, limit);
    }
}
