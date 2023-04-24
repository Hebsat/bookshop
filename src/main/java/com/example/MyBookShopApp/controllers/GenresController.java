package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.api.GenreDto;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.GenreService;
import com.example.MyBookShopApp.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;
    private final CookieService cookieService;
    private final RegistrationService registrationService;

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

    @ModelAttribute("booksInCart")
    public int booksInCart(@CookieValue(name = "cartContents", required = false) String contents) {
        return cookieService.getCountOfBooksInCookie(contents);
    }

    @ModelAttribute("booksInPostponed")
    public int booksInPostponed(@CookieValue(name = "postponedContents", required = false) String contents) {
        return cookieService.getCountOfBooksInCookie(contents);
    }

    @ModelAttribute("genreList")
    public List<GenreDto> genreEntityList() {
        return genreService.getGenres();
    }

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return registrationService.getCurrentUser();
    }

    @GetMapping
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/{slug}")
    public String getGenre(@PathVariable String slug, Model model) {

        return genreService.getGenreBySlug(slug, model);
    }

    @GetMapping("/{slug}/page")
    @ResponseBody
    public BooksListDto getGenreBooksPage(
            @PathVariable String slug,
            @RequestParam int offset,
            @RequestParam int limit) throws BookshopWrongParameterException {

        return genreService.getNextPageOfBooksByGenre(slug, offset, limit);
    }
}
