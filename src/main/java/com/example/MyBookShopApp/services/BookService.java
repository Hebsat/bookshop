package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.errors.WrongEntityException;
import com.example.MyBookShopApp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BookService {

     @Value("${bookshop.default.page}")
     private int defaultPage;
     @Value("${bookshop.default.size}")
     private int defaultSize;

     private final BookRepository bookRepository;

     @Autowired
     public BookService(BookRepository bookRepository) {
          this.bookRepository = bookRepository;
     }

     public Book saveBook(Book book) {
          return bookRepository.save(book);
     }

     public Page<Book> getPageOfRecommendedBooks(int page, int size) {
          Logger.getLogger(BookService.class.getName()).info("getPageOfRecommendedBooks started with page " + page + " and size " + size);
          Pageable nextPage = PageRequest.of(page, size, Sort.by(
                  Sort.Direction.DESC, "discount")
                  .and(Sort.by(Sort.Direction.ASC, "title")));
          return bookRepository.findAll(nextPage);
     }

     public Page<Book> getPageOfRecommendedBooks() {
          return getPageOfRecommendedBooks(defaultPage, defaultSize);
     }

     private Page<Book> getPageOfRecentBooks(LocalDateTime from, int page, int size, LocalDateTime to) {
          Logger.getLogger(BookService.class.getName()).info(
                  "getPageOfRecentBooks started from " + from + " to " + to + " with page " + page + " and size " + size);
          Pageable nextPage = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publicationDate"));
          return bookRepository.findAllByPublicationDateBetweenOrderByPublicationDateDesc(from, to, nextPage);
     }

     public Page<Book> getPageOfRecentBooks(String from, int page, int size, String to) {
          return getPageOfRecentBooks(DateTimeFormatter.fromDateTime(from), page, size, DateTimeFormatter.toDateTime(to));
     }

     public Page<Book> getPageOfRecentBooks(int page, int size, String to) {
          return getPageOfRecentBooks(LocalDateTime.now().minus(1,ChronoUnit.CENTURIES), page, size, DateTimeFormatter.toDateTime(to));
     }

     public Page<Book> getPageOfRecentBooks(String from, int page, int size) {
          return getPageOfRecentBooks(DateTimeFormatter.fromDateTime(from), page, size, LocalDateTime.now());
     }

     public Page<Book> getPageOfRecentBooks(int page, int size) {
          return getPageOfRecentBooks(LocalDateTime.now().minus(1,ChronoUnit.CENTURIES), page, size, LocalDateTime.now());
     }

     public Page<Book> getPageOfRecentBooks() {
          return getPageOfRecentBooks(defaultPage, defaultSize);
     }

     public Page<Book> getPageOfPopularBooks(int page, int size) {
          Logger.getLogger(BookService.class.getName()).info("getPageOfPopularBooks started with page " + page + " and size " + size);
          Pageable nextPage = PageRequest.of(page, size);
          return bookRepository.findAllByPopularityValue(nextPage);
     }

     public Page<Book> getPageOfPopularBooks() {
          return getPageOfPopularBooks(defaultPage, defaultSize);
     }


     public Book getBookBySlug(String slug) throws WrongEntityException {
          Logger.getLogger(BookService.class.getName()).info("getBookBySlug with slug: " + slug);
          return bookRepository.findBySlug(slug).orElseThrow(() -> new WrongEntityException("Книги с идентификатором " + slug + " не существует"));
     }

     public List<Book> getBooksBySlugList(Collection<String> slugs) {
          Logger.getLogger(BookService.class.getName()).info("getBooksBySlugList with slugs: " + slugs);
          return bookRepository.findBooksBySlugIn(slugs);
     }
}
