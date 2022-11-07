package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.Tag;
import com.example.MyBookShopApp.errors.WrongEntityException;
import com.example.MyBookShopApp.services.CookieService;
import com.example.MyBookShopApp.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books/tag")
public class TagsController {

    private final TagService tagService;
    private final CookieService cookieService;

    @Autowired
    public TagsController(TagService tagService, CookieService cookieService) {
        this.tagService = tagService;
        this.cookieService = cookieService;
    }

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
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

    @GetMapping("/{slug}")
    public String getTagBooks(@PathVariable String slug, Model model) throws WrongEntityException {
        Tag tag = tagService.getTagBySlug(slug);
        Page<Book> bookPage = tagService.getPageOfBooksByTag(tag);
        if (bookPage.getTotalPages() == 1) {
            model.addAttribute("lastPage", true);
        }
        model.addAttribute("pageTitle", "tag");
        model.addAttribute("pageTitlePart", tag.getName());
        model.addAttribute("tag", tag);
        model.addAttribute("books", bookPage);
        return "tags/index";
    }

    @GetMapping("/all")
    public String getAllTags(Model model) {
        model.addAttribute("pageTitle", "tags");
        model.addAttribute("tags", tagService.getTags());
        return "tags/all";
    }

    @GetMapping("/{slug}/page")
    @ResponseBody
    public BooksListDto getTagBooksPage(
            @PathVariable String slug,
            @RequestParam int offset,
            @RequestParam int limit) throws WrongEntityException {
        Tag tag = tagService.getTagBySlug(slug);
        return new BooksListDto(tagService.getPageOfBooksByTag(tag, offset, limit).getContent());
    }
}
