package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.errors.EmptySearchQueryException;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
public class SearchController {

    private final SearchService searchService;
    private final CookieService cookieService;

    @Autowired
    public SearchController(SearchService searchService, CookieService cookieService) {
        this.searchService = searchService;
        this.cookieService = cookieService;
    }

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

    @GetMapping({"/search", "/search/{query}"})
    public String searchPage(@RequestParam(required = false, defaultValue = "${bookshop.default.page}") int offset,
                             @RequestParam(required = false, defaultValue = "${bookshop.default.size}") int limit,
                             @PathVariable(value = "query", required = false) SearchQueryDto searchQueryDto, Model model) throws EmptySearchQueryException {
        if (searchQueryDto == null) {
            throw new EmptySearchQueryException("Empty search query!");
        }
        Page<Book> bookPage = searchService.getPageOfSearchResultBooks(searchQueryDto.getSearchQuery(), offset, limit);
        if (bookPage.getTotalPages() == 1) {
            model.addAttribute("lastPage", true);
        }
        model.addAttribute("searchQueryDto", searchQueryDto);
        model.addAttribute("searchResult", new BooksListDto((int) bookPage.getTotalElements(), bookPage.getContent()));
        return "search/index";
    }

    @GetMapping("/search/empty")
    public String searchEmpty() {
        return "search/index";
    }

    @GetMapping("/search/page/{query}")
    @ResponseBody
    public BooksListDto searchBooksPage(@RequestParam(required = false, defaultValue = "${bookshop.default.page}") int offset,
                                        @RequestParam(required = false, defaultValue = "${bookshop.default.size}") int limit,
                                        @PathVariable(value = "query", required = false) SearchQueryDto searchQueryDto) {
        Logger.getLogger(SearchController.class.getName())
                .info("REST query = " + searchQueryDto.getSearchQuery() + " with offset " + offset + " and limit " + limit);
        return new BooksListDto(searchService.getPageOfSearchResultBooks(searchQueryDto.getSearchQuery(), offset, limit).getContent());
    }
}
