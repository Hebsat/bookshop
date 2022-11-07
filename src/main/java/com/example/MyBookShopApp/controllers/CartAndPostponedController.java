package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.services.CookieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/changeBookStatus")
public class CartAndPostponedController {

    private final CookieService cookieService;

    public CartAndPostponedController(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @ModelAttribute("pageTitlePart")
    public String pageTitlePart() {
        return "UnknownUser";
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

    @GetMapping({"/cart", "/postponed"})
    public String cartAndPostponedMainPage(
            HttpServletRequest request,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            Model model) {
        List<String> params = cookieService.getRequestValues(request, cartContents, postponedContents);
        model.addAttribute("pageTitle", params.get(0));
        if (cookieService.validateCookie(params.get(1))) {
            List<Book> booksFromCookie = cookieService.getBooksFromCookie(params.get(1));
            model.addAttribute("books", booksFromCookie);
            model.addAttribute("priceOld", booksFromCookie.stream().mapToDouble(Book::getPrice).sum());
            model.addAttribute("price",booksFromCookie.stream()
                    .mapToDouble(book -> book.getPrice() - (book.getPrice() * book.getDiscount() / 100)).sum());
        }
        return params.get(0);
    }

    @PostMapping({"/cart/remove/{slug}", "/postponed/remove/{slug}"})
    private String removeBook(
            @PathVariable String slug,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            HttpServletResponse response, HttpServletRequest request) {
        List<String> params = cookieService.getRequestValues(request, cartContents, postponedContents);
        if (cookieService.validateCookie(params.get(1))) {
            response.addCookie(
                    cookieService.removeContentFromCookie(params.get(1), slug, params.get(2), "/"));
        }
        return "redirect:/changeBookStatus/" + params.get(0);
    }

    @PostMapping({"/cart/add/{slug}", "/postponed/add/{slug}"})
    private String addBook(
            @PathVariable String slug,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            HttpServletResponse response, HttpServletRequest request) {
        List<String> params = cookieService.getRequestValues(request, cartContents, postponedContents);
        List<Cookie> cookies = (cookieService.addContentToCookie(params.get(1), params.get(3), slug, params.get(2), params.get(4), "/"));
        response.addCookie(cookies.get(0));
        response.addCookie(cookies.get(1));
        return "redirect:/books/" + slug;
    }
}
