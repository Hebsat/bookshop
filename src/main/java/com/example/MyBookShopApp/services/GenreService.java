package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.Genre;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.errors.WrongEntityException;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class GenreService {

    @Value("${bookshop.default.page}")
    private int defaultPage;
    @Value("${bookshop.default.size}")
    private int defaultSize;

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    public Genre getGenreById(int id) throws BookshopWrongParameterException {
        return genreRepository.findById(id)
                .orElseThrow(() -> new BookshopWrongParameterException("Genre with the specified id " + id + " does not exist"));
    }

    public Genre getGenreBySlug(String slug) throws WrongEntityException {
        Logger.getLogger(GenreService.class.getName()).info("getGenreBySlug " + slug);
        return genreRepository.findGenreBySlug(slug)
                .orElseThrow(() -> new WrongEntityException("Жанра с идентификатором " + slug + " не существует"));
    }

    public List<Genre> getGenres() {
        Logger.getLogger(GenreService.class.getName()).info("getGenres");
        List<Genre> genreList = genreRepository.findGenresByParentGenreIsNull();
        genreList.forEach(this::addBooksFromEmbeddedGenres);
        return genreList;
    }

    public Page<Book> getPageOfBooksByGenre(Genre genre, int page, int size) {
        Logger.getLogger(GenreService.class.getName()).info(
                "getPageOfBooksByGenre " + genre + " with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        return bookRepository.findBooksByGenre(genre, nextPage);
    }

    public Page<Book> getPageOfBooksByGenreIncludedEmbeddedGenres(Genre genre, int page, int size) {
        Logger.getLogger(GenreService.class.getName()).info(
                "getPageOfBooksByGenreIncludedEmbeddedGenres " + genre + " with page " + page + " and size " + size);
        Pageable nextPage = PageRequest.of(page, size);
        List<Genre> genreList = getAllEmbeddedGenres(genre);
        return bookRepository.findBooksByGenreIn(genreList, nextPage);
    }

    public Page<Book> getPageOfBooksByGenreIncludedEmbeddedGenres(Genre genre) {
        return getPageOfBooksByGenreIncludedEmbeddedGenres(genre, defaultPage, defaultSize);
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

    public Map<String, String> getGenreBreadCrumbs(Genre genre) {
        List<Genre> genres = new ArrayList<>();
        Map<String, String> genreNames = new LinkedHashMap<>();
        Genre currentGenre = genre;
        while (currentGenre.getParentGenre() != null) {
            currentGenre = currentGenre.getParentGenre();
            genres.add(0, currentGenre);
        }
        genres.forEach(g -> genreNames.put(g.getName(), g.getSlug()));
        return genreNames;
    }
}
