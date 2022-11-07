package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.data.main.Genre;
import com.example.MyBookShopApp.errors.WrongEntityException;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/genres")
public class GenresController {

    private final GenreService genreService;
    private final CookieService cookieService;

    @Autowired
    public GenresController(GenreService genreService, CookieService cookieService) {
        this.genreService = genreService;
        this.cookieService = cookieService;
    }

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
    public List<Genre> genreEntityList() {
        return genreService.getGenres();
    }

    @GetMapping
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/{slug}")
    public String getGenre(@PathVariable String slug, Model model) throws WrongEntityException {
        Genre genre = genreService.getGenreBySlug(slug);
        model.addAttribute("genre", genre);
        model.addAttribute("pageTitle", "genre");
        model.addAttribute("pageTitlePart", genre.getName());
        model.addAttribute("bookList", genreService.getPageOfBooksByGenreIncludedEmbeddedGenres(genre).getContent());
        model.addAttribute("breadCrumbs", genreService.getGenreBreadCrumbs(genre));
        return "genres/slug";
    }

    @GetMapping("/{slug}/page")
    @ResponseBody
    public BooksListDto getGenreBooksPage(
            @PathVariable String slug,
            @RequestParam int offset,
            @RequestParam int limit) throws WrongEntityException {
        Genre genre = genreService.getGenreBySlug(slug);
        return new BooksListDto(genreService.getPageOfBooksByGenreIncludedEmbeddedGenres(genre, offset, limit).getContent());
    }
}
