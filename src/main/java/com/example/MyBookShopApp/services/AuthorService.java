package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.AuthorDto;
import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.services.mappers.AuthorMapper;
import com.example.MyBookShopApp.services.mappers.BookMapper;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    @Value("${bookshop.default.page}")
    private int defaultPage;
    @Value("${bookshop.default.size}")
    private int defaultSize;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    public Map<String, List<AuthorDto>> getAuthorData() {
        Logger.getLogger(AuthorService.class.getName()).info(
                "getAuthorData");
        List<AuthorDto> authors = authorRepository.findAll().stream().map(authorMapper::convertAuthorToAuthorDtoLight).collect(Collectors.toList());
        return authors.stream().collect(Collectors.groupingBy(a -> a.getName().substring(0, 1)));
    }

    public BooksListDto getPageOfBooksByAuthor(String slug, int page, int size) throws BookshopWrongParameterException {
        Logger.getLogger(AuthorService.class.getName()).info(
                "getPageOfBooksByAuthor: " + slug + " with page " + page + " and size " + size);
        Author author = authorRepository.findAuthorBySlug(slug)
                .orElseThrow(() -> new BookshopWrongParameterException("Автора с идентификатором " + slug + " не существует"));
        return getBookListDto(author, page, size);
    }

    public String getFirstPageOfBooksByAuthor(String slug, Model model) {
        Optional<Author> author = authorRepository.findAuthorBySlug(slug);
        model.addAttribute("topBarIdentifier", "genres");
        model.addAttribute("pageTitle", "books");
        if (author.isEmpty()) {
            model.addAttribute("error", "Автора с идентификатором " + slug + " не существует");
            return "/errors/404";
        }
        BooksListDto booksListDto = getBookListDto(author.get(), defaultPage, defaultSize);
        if (booksListDto.getTotalPages() == 1) {
            model.addAttribute("lastPage", true);
        }
        model.addAttribute("pageTitlePart", author.get().getName());
        model.addAttribute("pageHeadDescription",
                "Книги автора " + author.get().getName() + ": " + Arrays.toString(author.get().getBookList().stream().map(Book::getTitle).toArray()));
        model.addAttribute("author", author.get());
        model.addAttribute("books", booksListDto.getBooks());
        return "/books/author";
    }

    public String getAuthorBySlug(String slug, Model model) {
        Optional<Author> author = authorRepository.findAuthorBySlug(slug);
        model.addAttribute("pageTitle", "author");
        model.addAttribute("topBarIdentifier", "authors");
        if (author.isEmpty()) {
            model.addAttribute("error", "Автора с идентификатором " + slug + " не существует");
            return "/errors/404";
        }
        AuthorDto authorDto = authorMapper.convertAuthorToAuthorDtoFull(author.get());
        model.addAttribute("author", authorDto);
        model.addAttribute("pageTitlePart", authorDto.getName());
        model.addAttribute("pageHeadDescription",
                authorDto.getName() + ". Биография: " + authorDto.getDescription().substring(0, 100) + "...");
        return "authors/slug";
    }

    private BooksListDto getBookListDto(Author author, int page, int size) {
        Pageable nextPage = PageRequest.of(page, size);
        Page<Book> books = bookRepository.findBooksByAuthorListContains(author, nextPage);
        return BooksListDto.builder()
                .totalPages(books.getTotalPages())
                .count((int) books.getTotalElements())
                .books(books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                .build();
    }
}
