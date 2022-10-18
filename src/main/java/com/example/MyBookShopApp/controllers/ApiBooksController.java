package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.config.SpringfoxConfig;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/books")
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
    @ApiOperation(value = "Method to get list of recommended books", notes = "${bookshop.notes.recommended}")
    public ResponseEntity<List<Book>> getRecommendedBooks(@RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
                                                          @RequestParam(name = "limit", defaultValue = "20", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookData(offset, limit).getContent());
    }

    @GetMapping("/recent")
    @ApiOperation(value = "Method to get list of recent books", notes = "${bookshop.notes.recent}")
    public ResponseEntity<List<Book>> getRecentBooks(@RequestParam(name = "from", required = false) LocalDateTime from,
                                                     @RequestParam(name = "to", required = false) LocalDateTime to,
                                                     @RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
                                                     @RequestParam(name = "limit", defaultValue = "20", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getRecentBooks());
    }

    @GetMapping("/popular")
    @ApiOperation(value = "Method to get list of popular books", notes = "${bookshop.notes.popular}")
    public ResponseEntity<List<Book>> getPopularBooks(@RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
                                                      @RequestParam(name = "limit", defaultValue = "20", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getPopularBooks());
    }

    @GetMapping("/genre/{id}")
    @ApiOperation(value = "Method to get list books by genre with chosen id", notes = "${bookshop.notes.genres}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable int id,
                                                      @RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
                                                      @RequestParam(name = "limit", defaultValue = "20", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getPopularBooks());
    }

    @GetMapping("/author/{id}")
    @ApiOperation(value = "Method to get list books by author with chosen id", notes = "${bookshop.notes.authors}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable int id,
                                                       @RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
                                                       @RequestParam(name = "limit", defaultValue = "20", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthorById(id).getBookList());
    }

    @GetMapping("/tag/{id}")
    @ApiOperation(value = "Method to get list books by tag with chosen id", notes = "${bookshop.notes.tags}")
    public ResponseEntity<List<Book>> getBooksByTag(@PathVariable int id,
                                                    @RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
                                                    @RequestParam(name = "limit", defaultValue = "20", required = false) int limit) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getPopularBooks());
    }
}
