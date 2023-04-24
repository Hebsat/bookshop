package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.BookDto;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.api.TagDto;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.RegistrationService;
import com.example.MyBookShopApp.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;
    private final CookieService cookieService;
    private final RegistrationService registrationService;

    @ModelAttribute("recommendedBooks")
    public List<BookDto> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks().getBooks();
    }

    @ModelAttribute("recentBooks")
    public List<BookDto> recentBooks() {
        return bookService.getPageOfRecentBooks().getBooks();
    }

    @ModelAttribute("popularBooks")
    public List<BookDto> popularBooks() {
        return bookService.getPageOfPopularBooks().getBooks();
    }

    @ModelAttribute("tags")
    public List<TagDto> tags() {
        return tagService.getTags();
    }

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "main";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "It's over 9000  книг в магазине Bookshop!";
    }

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "main";
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

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}
