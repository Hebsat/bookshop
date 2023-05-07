package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.*;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class AuthorizeController {

    private final CookieService cookieService;
    private final RegistrationService registrationService;

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return "sign_in";
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

    @GetMapping("/signin")
    public String signIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ApiSimpleResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        return new ApiSimpleResponse(true);
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ApiSimpleResponse handleApproveContact(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        return new ApiSimpleResponse(true);
    }

    @PostMapping("/reg")
//    @ResponseBody
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        model.addAttribute("regOk", registrationService.registerNewUser(registrationForm));
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiSimpleResponse handleLogin(@RequestBody ContactConfirmationPayload contactConfirmationPayload, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", registrationService.jwtLogin(contactConfirmationPayload));
        response.addCookie(cookie);
        return new ApiSimpleResponse(true);
    }

    @GetMapping("/my")
    public String handleMy() {
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile() {
        return "profile";
    }
}
