package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.data.Tag;
import com.example.MyBookShopApp.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books/tag")
public class TagsController {

    private final TagService tagService;

    @Autowired
    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @ModelAttribute("topBarIdentifier")
    public String topBarIdentifier() {
        return "main";
    }

    @ModelAttribute("pageHeadDescription")
    public String pageHeadDescription() {
        return "It's over 9000  книг в магазине Bookshop!";
    }

    @ModelAttribute("searchQueryDto")
    public SearchQueryDto searchQueryDto() {
        return new SearchQueryDto();
    }

    @GetMapping("/{slug}")
    public String getTagBooks(@PathVariable String slug, Model model) {
        Tag tag = tagService.getTagBySlug(slug);
        model.addAttribute("pageTitle", "tag");
        model.addAttribute("pageTitlePart", tag.getName());
        model.addAttribute("tag", tag);
        model.addAttribute("books", tagService.getPageOfBooksByTag(slug));
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
            @RequestParam int limit) {
        return new BooksListDto(tagService.getPageOfBooksByTag(slug, offset, limit).getContent());
    }
}
