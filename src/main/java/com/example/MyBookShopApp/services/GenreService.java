package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.api.GenreDto;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.Genre;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.services.mappers.BookMapper;
import com.example.MyBookShopApp.services.mappers.GenreMapper;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    @Value("${bookshop.default.page}")
    private int defaultPage;
    @Value("${bookshop.default.size}")
    private int defaultSize;

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreMapper genreMapper;

    public String getGenreBySlug(String slug, Model model) {
        Optional<Genre> genre = genreRepository.findGenreBySlug(slug);
        model.addAttribute("pageTitle", "genre");
        if (genre.isEmpty()) {
            model.addAttribute("error", "Жанра с идентификатором " + slug + " не существует");
            return "/errors/404";
        }
        model.addAttribute("genre", genreMapper.convertGenreToGenreDto(genre.get()));
        model.addAttribute("pageTitlePart", genre.get().getName());
        model.addAttribute("bookList", getPageOfBooksByGenreIncludedEmbeddedGenres(genre.get(), defaultPage, defaultSize).getBooks());
        model.addAttribute("breadCrumbs", getGenreBreadCrumbs(genre.get()));
        return "genres/slug";
    }

    public List<GenreDto> getGenres() {
        Logger.getLogger(GenreService.class.getName()).info("getGenres");
        List<Genre> genreList = genreRepository.findGenresByParentGenreIsNull();
        genreList.forEach(this::addBooksFromEmbeddedGenres);
        return genreList.stream().map(genreMapper::convertGenreToGenreDto).collect(Collectors.toList());
    }

    private BooksListDto getPageOfBooksByGenreIncludedEmbeddedGenres(Genre genre, int page, int size) {
        Logger.getLogger(GenreService.class.getName()).info(
                "getPageOfBooksByGenreIncludedEmbeddedGenres " + genre + " with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        List<Genre> genreList = getAllEmbeddedGenres(genre);
        Page<Book> books = bookRepository.findBooksByGenreIn(genreList, nextPage);
        return BooksListDto.builder()
                .totalPages(books.getTotalPages())
                .count((int) books.getTotalElements())
                .books(books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                .build();
    }

    public BooksListDto getNextPageOfBooksByGenre(String slug, int page, int size) throws BookshopWrongParameterException {
        Genre genre = genreRepository.findGenreBySlug(slug)
                .orElseThrow(() -> new BookshopWrongParameterException("Жанра с идентификатором " + slug + " не существует"));
        return getPageOfBooksByGenreIncludedEmbeddedGenres(genre, page, size);
    }

    public BooksListDto getPageOfBooksByGenreId(int id, int page, int size) throws BookshopWrongParameterException {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new BookshopWrongParameterException("Жанра с идентификатором " + id + " не существует"));
        return getPageOfBooksByGenreIncludedEmbeddedGenres(genre, page, size);
    }

    private void addBooksFromEmbeddedGenres(Genre genre) {
        if (genre.getEmbeddedGenreList().isEmpty()) {
            genre.setBookListIncludedBooksOfEmbeddedGenres(genre.getBookList());
            return;
        }
        List<Book> bookList = new ArrayList<>(genre.getBookList());
        genre.getEmbeddedGenreList().forEach(embGenre ->{
            addBooksFromEmbeddedGenres(embGenre);
            bookList.addAll(embGenre.getBookListIncludedBooksOfEmbeddedGenres());
        });
        genre.setBookListIncludedBooksOfEmbeddedGenres(bookList);
    }

    private List<Genre> getAllEmbeddedGenres(Genre genre) {
        if (genre.getEmbeddedGenreList().isEmpty()) {
            return List.of(genre);
        }
        List<Genre> genreList = new ArrayList<>(List.of(genre));
        genre.getEmbeddedGenreList().forEach(embGenre -> genreList.addAll(getAllEmbeddedGenres(embGenre)));
        return genreList;
    }

    private Map<String, String> getGenreBreadCrumbs(Genre genre) {
        Deque<Genre> genres = new ArrayDeque<>();
        Map<String, String> genreNames = new LinkedHashMap<>();
        Genre currentGenre = genre;
        while (currentGenre.getParentGenre() != null) {
            currentGenre = currentGenre.getParentGenre();
            genres.addFirst(currentGenre);
        }
        genres.forEach(g -> genreNames.put(g.getName(), g.getSlug()));
        return genreNames;
    }
}
