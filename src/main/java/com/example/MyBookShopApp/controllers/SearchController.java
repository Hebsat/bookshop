package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
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

    @GetMapping({"/search", "/search/{query}"})
    public String searchPage(@RequestParam(required = false, defaultValue = "${bookshop.default.offset}") int offset,
                             @RequestParam(required = false, defaultValue = "${bookshop.default.limit}") int limit,
                             @PathVariable(value = "query", required = false) SearchQueryDto searchQueryDto, Model model) {
        if (searchQueryDto == null) {
            model.addAttribute("searchResult", new BooksListDto(new ArrayList<>()));
        } else {
            model.addAttribute("searchQueryDto", searchQueryDto);
            model.addAttribute("searchResult", new BooksListDto(
                    searchService.getCountOfSearchResult(searchQueryDto.getSearchQuery()),
                    searchService.getPageOfSearchResultBooks(searchQueryDto.getSearchQuery(), offset, limit).getContent()));
        }
        return "search/index";
    }

    @GetMapping("/search/page/{query}")
    @ResponseBody
    public BooksListDto searchBooksPage(@RequestParam(required = false, defaultValue = "0") int offset,
                                        @RequestParam(required = false, defaultValue = "10") int limit,
                                        @PathVariable(value = "query", required = false) SearchQueryDto searchQueryDto) {
        Logger.getLogger(SearchController.class.getName()).info("REST query = " + searchQueryDto.getSearchQuery() + " with offset " + offset + " and limit " + limit);
        return new BooksListDto(searchService.getPageOfSearchResultBooks(searchQueryDto.getSearchQuery(), offset, limit).getContent());
    }
}
