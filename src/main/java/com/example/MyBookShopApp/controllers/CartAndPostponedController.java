package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.SearchQueryDto;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.errors.SomethingWrongException;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.RegistrationService;
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
    private final RegistrationService registrationService;

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

    @ModelAttribute("currentUser")
    public UserDto currentUser() {
        return registrationService.getCurrentUser();
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
            @RequestParam List<String> booksIds,
            @RequestParam String status,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            HttpServletResponse response) {

        return cookieService.changeBookStatus(booksIds, status, response,cartContents, postponedContents);
    }

    @PostMapping("/api/changeBookStatus")
    @ResponseBody
    public ApiSimpleResponse allToCart(
            @CookieValue(name = "cartContents", required = false) String cartContents,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            HttpServletResponse response) {

        return cookieService.addAllBooksFromPostponedToCart(response, postponedContents, cartContents);
    }
}
