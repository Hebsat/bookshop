package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.config.SpringfoxConfig;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.data.ErrorResponseDto;
import com.example.MyBookShopApp.data.SearchQueryDto;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@PropertySource("/descriptions/api.properties")
@Api(tags = {SpringfoxConfig.TAG_1})
public class ApiBooksController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public ApiBooksController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/recommended")
    @ApiOperation(value = "${bookshop.value.recommended}", notes = "${bookshop.notes.recommended}")
    public ResponseEntity<BooksListDto> getRecommendedBooks(
            @ApiParam("${bookshop.param.offset}") @RequestParam(defaultValue = "${bookshop.default.offset}", required = false) int offset,
            @ApiParam("${bookshop.param.limit}")  @RequestParam(defaultValue = "${bookshop.default.limit}", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(new BooksListDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent()));
    }

    @GetMapping("/recent")
    @ApiOperation(value = "${bookshop.value.recent}", notes = "${bookshop.notes.recent}")
    public ResponseEntity<BooksListDto> getRecentBooks(
            @ApiParam(value = "${bookshop.param.from}", example = "25-11-2020")  @RequestParam(name = "from", required = false) String from,
            @ApiParam(value = "${bookshop.param.to}", example = "25-11-2020")    @RequestParam(name = "to", required = false) String to,
            @ApiParam("${bookshop.param.offset}") @RequestParam(defaultValue = "${bookshop.default.offset}", required = false) int offset,
            @ApiParam("${bookshop.param.limit}")  @RequestParam(defaultValue = "${bookshop.default.limit}", required = false) int limit) {
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
            @ApiParam("${bookshop.param.offset}") @RequestParam(defaultValue = "${bookshop.default.offset}", required = false) int offset,
            @ApiParam("${bookshop.param.limit}")  @RequestParam(defaultValue = "${bookshop.default.limit}", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(new BooksListDto(bookService.getPageOfPopularBooks(offset, limit).getContent()));
    }

    @GetMapping("/genre/{id}")
    @ApiOperation(value = "${bookshop.value.genres}", notes = "${bookshop.notes.genres}")
    public ResponseEntity<BooksListDto> getBooksByGenre(
            @ApiParam(value = "${bookshop.param.id}", example = "1") @PathVariable int id,
            @ApiParam("${bookshop.param.offset}") @RequestParam(defaultValue = "${bookshop.default.offset}", required = false) int offset,
            @ApiParam("${bookshop.param.limit}")  @RequestParam(defaultValue = "${bookshop.default.limit}", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(new BooksListDto(bookService.getPageOfPopularBooks(offset, limit).getContent()));
    }

    @GetMapping("/author/{id}")
    @ApiOperation(value = "${bookshop.value.authors}", notes = "${bookshop.notes.authors}")
    public ResponseEntity<BooksListDto> getBooksByAuthor(
            @ApiParam(value = "${bookshop.param.id}", example = "1") @PathVariable int id,
            @ApiParam("${bookshop.param.offset}") @RequestParam(defaultValue = "${bookshop.default.offset}", required = false) int offset,
            @ApiParam("${bookshop.param.limit}")  @RequestParam(defaultValue = "${bookshop.default.limit}", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(new BooksListDto(authorService.getAuthorById(id).getBookList()));
    }

    @GetMapping("/tag/{id}")
    @ApiOperation(value = "${bookshop.value.tags}", notes = "${bookshop.notes.tags}")
    public ResponseEntity<BooksListDto> getBooksByTag(
            @ApiParam(value = "${bookshop.param.id}", example = "1") @PathVariable int id,
            @ApiParam("${bookshop.param.offset}") @RequestParam(defaultValue = "${bookshop.default.offset}", required = false) int offset,
            @ApiParam("${bookshop.param.limit}")  @RequestParam(defaultValue = "${bookshop.default.limit}", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(new BooksListDto(bookService.getPageOfPopularBooks(offset, limit).getContent()));
    }
}
