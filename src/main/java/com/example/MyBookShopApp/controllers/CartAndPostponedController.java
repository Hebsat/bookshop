package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.errors.SomethingWrongException;
import com.example.MyBookShopApp.services.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/changeBookStatus")
@RequiredArgsConstructor
public class CartAndPostponedController {

    private final CookieService cookieService;

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
            Model model) throws SomethingWrongException {

        return cookieService.getCartAndPostponedMainPage(request, cartContents, postponedContents, model);
    }

    @PostMapping
    @ResponseBody
    public ApiSimpleResponse changeBookStatus(
//            @PathVariable String slug,
            @RequestParam List<String> booksIds,
            @RequestParam String status,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            HttpServletResponse response) {

        return cookieService.changeBookStatus(booksIds, status, response,cartContents, postponedContents);
    }

//    @PostMapping({"/cart/remove/{slug}", "/postponed/remove/{slug}"})
//    @ResponseBody
//    private ApiSimpleResponse removeBook(
//            @PathVariable String slug,
//            @CookieValue(name = "cartContents", required = false) String cartContents,
//            @CookieValue(name = "postponedContents", required = false) String postponedContents,
//            HttpServletResponse response, HttpServletRequest request) {
//
//        cookieService.removeBookFromCookie(response, request, slug, cartContents, postponedContents);
//        return new ApiSimpleResponse(true);
//    }
//
//    @PostMapping({"/cart/add/{slug}", "/postponed/add/{slug}"})
//    @ResponseBody
//    private ApiSimpleResponse addBook(
//            @PathVariable String slug,
//            @CookieValue(name = "cartContents", required = false) String cartContents,
//            @CookieValue(name = "postponedContents", required = false) String postponedContents,
//            HttpServletResponse response, HttpServletRequest request) {
//
//        cookieService.addBookToCookie(response, request, slug, cartContents, postponedContents);
//        return new ApiSimpleResponse(true);
//    }

    @PostMapping("/api/changeBookStatus")
    @ResponseBody
    public ApiSimpleResponse allToCart(
            @CookieValue(name = "cartContents", required = false) String cartContents,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            HttpServletResponse response) {

        return cookieService.addAllBooksFromPostponedToCart(response, postponedContents, cartContents);
    }
}
