package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.config.SpringfoxConfig;
import com.example.MyBookShopApp.data.ApiResponse;
import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.Genre;
import com.example.MyBookShopApp.data.main.Tag;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.GenreService;
import com.example.MyBookShopApp.services.TagService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@PropertySource("/descriptions/api.properties")
@Api(tags = {SpringfoxConfig.TAG_1})
public class ApiBooksController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final TagService tagService;

    @Autowired
    public ApiBooksController(BookService bookService, AuthorService authorService, GenreService genreService, TagService tagService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.tagService = tagService;
    }

    @GetMapping("/recommended")
    @ApiOperation(value = "${bookshop.value.recommended}", notes = "${bookshop.notes.recommended}")
    public ResponseEntity<BooksListDto> getRecommendedBooks(
            @ApiParam("${bookshop.param.page}") @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("${bookshop.param.size}") @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(new BooksListDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent()));
    }

    @GetMapping("/recent")
    @ApiOperation(value = "${bookshop.value.recent}", notes = "${bookshop.notes.recent}")
    public ResponseEntity<BooksListDto> getRecentBooks(
            @ApiParam(value = "${bookshop.param.from}", example = "25-11-2020")  @RequestParam(name = "from", required = false) String from,
            @ApiParam(value = "${bookshop.param.to}", example = "25-11-2020")    @RequestParam(name = "to", required = false) String to,
            @ApiParam("${bookshop.param.page}") @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("${bookshop.param.size}") @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) {
        if (from != null && to != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BooksListDto(bookService.getPageOfRecentBooks(from, offset, limit, to).getContent()));
        } else if (to != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BooksListDto(bookService.getPageOfRecentBooks(offset, limit, to).getContent()));
        } else if (from != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BooksListDto(bookService.getPageOfRecentBooks(from, offset, limit).getContent()));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new BooksListDto(bookService.getPageOfRecentBooks(offset, limit).getContent()));
        }
    }

    @GetMapping("/popular")
    @ApiOperation(value = "${bookshop.value.popular}", notes = "${bookshop.notes.popular}")
    public ResponseEntity<BooksListDto> getPopularBooks(
            @ApiParam("${bookshop.param.page}") @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("${bookshop.param.size}") @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(new BooksListDto(bookService.getPageOfPopularBooks(offset, limit).getContent()));
    }

    @GetMapping("/genre/{id}")
    @ApiOperation(value = "${bookshop.value.genres}", notes = "${bookshop.notes.genres}")
    public ResponseEntity<BooksListDto> getBooksByGenre(
            @ApiParam(value = "${bookshop.param.id}", example = "1") @PathVariable int id,
            @ApiParam("${bookshop.param.page}") @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("${bookshop.param.size}") @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {
        Genre genre = genreService.getGenreById(id);
        Page<Book> bookPage = genreService.getPageOfBooksByGenre(genre, offset, limit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BooksListDto((int) bookPage.getTotalElements(), bookPage.getContent()));
    }

    @GetMapping("/author/{id}")
    @ApiOperation(value = "${bookshop.value.authors}", notes = "${bookshop.notes.authors}")
    public ResponseEntity<BooksListDto> getBooksByAuthor(
            @ApiParam(value = "${bookshop.param.id}", example = "1") @PathVariable int id,
            @ApiParam("${bookshop.param.page}") @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("${bookshop.param.size}") @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {
        Author author = authorService.getAuthorById(id);
        Page<Book> bookPage = authorService.getPageOfBooksByAuthor(author, offset, limit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BooksListDto((int) bookPage.getTotalElements(), bookPage.getContent()));
    }

    @GetMapping("/tag/{id}")
    @ApiOperation(value = "${bookshop.value.tags}", notes = "${bookshop.notes.tags}")
    public ResponseEntity<BooksListDto> getBooksByTag(
            @ApiParam(value = "${bookshop.param.id}", example = "1") @PathVariable int id,
            @ApiParam("${bookshop.param.page}") @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("${bookshop.param.size}") @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {
        Tag tag = tagService.getTagById(id);
        Page<Book> bookPage = tagService.getPageOfBooksByTag(tag, offset, limit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BooksListDto((int) bookPage.getTotalElements(), bookPage.getContent()));
    }
}
