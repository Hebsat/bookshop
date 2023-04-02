package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.BookDto;
import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.config.SpringfoxConfig;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.GenreService;
import com.example.MyBookShopApp.services.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@Api(tags = {SpringfoxConfig.TAG_1})
@RequiredArgsConstructor
public class ApiBooksController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final TagService tagService;

    @GetMapping("/recommended")
    @ApiOperation(value = "Method to get list of recommended books",
            notes = "the list of recommended for current user books if authorized or any user if not authorized")
    public BooksListDto getRecommendedBooks(
            @ApiParam("page number of the book list")
            @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("the number of displayed books in each page")
            @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {

        return bookService.setLinksToBooks(bookService.getPageOfRecommendedBooks(offset, limit));
    }

    @GetMapping("/recent")
    @ApiOperation(value = "Method to get list of recent books",
            notes = "the list of books published after \"from\" date and before \"to\" date if that parameters are specified and before current time if not")
    public BooksListDto getRecentBooks(
            @ApiParam(value = "date in format dd-MM-yyyy from which the list of books is displayed", example = "25-11-2020")
            @RequestParam(name = "from", required = false) String from,
            @ApiParam(value = "date in format dd-MM-yyyy by which the list of books is displayed", example = "25-11-2020")
            @RequestParam(name = "to", required = false) String to,
            @ApiParam("page number of the book list")
            @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("the number of displayed books in each page")
            @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {

        return bookService.setLinksToBooks(bookService.getPageOfRecentBooks(from, offset, limit, to));
    }

    @GetMapping("/popular")
    @ApiOperation(value = "Method to get list of popular books", notes = "the list of the most popular books in bookshop")
    public BooksListDto getPopularBooks(
            @ApiParam("page number of the book list")
            @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("the number of displayed books in each page")
            @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {

        return bookService.setLinksToBooks(bookService.getPageOfPopularBooks(offset, limit));
    }

    @GetMapping("/genre/{id}")
    @ApiOperation(value = "Method to get list books by genre with chosen id", notes = "all books of genre with current id")
    public BooksListDto getBooksByGenre(
            @ApiParam(value = "the number of required entity", example = "1") @PathVariable int id,
            @ApiParam("page number of the book list")
            @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("the number of displayed books in each page")
            @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {

        return bookService.setLinksToBooks(genreService.getPageOfBooksByGenreId(id, offset, limit));
    }

    @GetMapping("/author/{id}")
    @ApiOperation(value = "Method to get list books by author with chosen id", notes = "all books of author with current id")
    public BooksListDto getBooksByAuthor(
            @ApiParam(value = "the number of required entity", example = "1") @PathVariable int id,
            @ApiParam("page number of the book list")
            @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("the number of displayed books in each page")
            @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {

        return bookService.setLinksToBooks(authorService.getPageOfBooksByAuthorId(id, offset, limit));
    }

    @GetMapping("/tag/{id}")
    @ApiOperation(value = "Method to get list books by tag with chosen id", notes = "$all books of tag with current id")
    public BooksListDto getBooksByTag(
            @ApiParam(value = "the number of required entity", example = "1") @PathVariable int id,
            @ApiParam("page number of the book list")
            @RequestParam(defaultValue = "${bookshop.default.page}", required = false) int offset,
            @ApiParam("the number of displayed books in each page")
            @RequestParam(defaultValue = "${bookshop.default.size}", required = false) int limit) throws BookshopWrongParameterException {

        return bookService.setLinksToBooks(tagService.getPageOfBooksByTagId(id, offset, limit));
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Integer id) throws BookshopWrongParameterException {

        return bookService.getBookById(id);
    }
}
