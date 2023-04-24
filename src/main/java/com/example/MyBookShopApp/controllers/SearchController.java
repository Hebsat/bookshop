package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.RegistrationService;
import com.example.MyBookShopApp.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final CookieService cookieService;
    private final RegistrationService registrationService;

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "search";
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

    @GetMapping({"/search", "/search/{query}"})
    public String searchPage(@RequestParam(required = false, defaultValue = "${bookshop.default.page}") int offset,
                             @RequestParam(required = false, defaultValue = "${bookshop.default.size}") int limit,
                             @PathVariable(value = "query", required = false) SearchQueryDto searchQueryDto, Model model) {
        if (searchQueryDto == null) {
            model.addAttribute("emptySearch", "Empty search query!");
        } else {
            BooksListDto booksListDto = searchService.getPageOfSearchResultBooks(searchQueryDto.getSearchQuery(), offset, limit);
            if (booksListDto.getTotalPages() == 1) {
                model.addAttribute("lastPage", true);
            }
            model.addAttribute("searchQueryDto", searchQueryDto);
            model.addAttribute("searchResult", booksListDto);
        }
        return "search/index";
    }

    @GetMapping("/search/page/{query}")
    @ResponseBody
    public BooksListDto searchBooksPage(@RequestParam(required = false, defaultValue = "${bookshop.default.page}") int offset,
                                        @RequestParam(required = false, defaultValue = "${bookshop.default.size}") int limit,
                                        @PathVariable(value = "query") SearchQueryDto searchQueryDto) {
        Logger.getLogger(SearchController.class.getName())
                .info("REST query = " + searchQueryDto.getSearchQuery() + " with offset " + offset + " and limit " + limit);
        return searchService.getPageOfSearchResultBooks(searchQueryDto.getSearchQuery(), offset, limit);
    }
}
