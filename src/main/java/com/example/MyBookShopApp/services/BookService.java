package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Book;
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
import java.util.logging.Logger;

@Service
public class BookService {

     @Value("${bookshop.default.offset}")
     private int defaultOffset;
     @Value("${bookshop.default.limit}")
     private int defaultLimit;

     private final BookRepository bookRepository;

     @Autowired
     public BookService(BookRepository bookRepository) {
          this.bookRepository = bookRepository;
     }

     public Page<Book> getPageOfRecommendedBooks(int offset, int limit) {
          Logger.getLogger(BookService.class.getName()).info("getPageOfRecommendedBooks started with offset " + offset + " and limit " + limit);
          Pageable nextPage = PageRequest.of(offset, limit, Sort.by(
                  Sort.Direction.DESC, "discount")
                  .and(Sort.by(Sort.Direction.ASC, "title")));
          return bookRepository.findAll(nextPage);
     }

     public Page<Book> getPageOfRecommendedBooks() {
          return getPageOfRecommendedBooks(defaultOffset, defaultLimit);
     }

     private Page<Book> getPageOfRecentBooks(LocalDateTime from, int offset, int limit, LocalDateTime to) {
          Logger.getLogger(BookService.class.getName()).info(
                  "getPageOfRecentBooks started from " + from + " to " + to + " with offset " + offset + " and limit " + limit);
          Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "publicationDate"));
          return bookRepository.findAllByPublicationDateBetweenOrderByPublicationDateDesc(from, to, nextPage);
     }

     public Page<Book> getPageOfRecentBooks(String from, int offset, int limit, String to) {
          return getPageOfRecentBooks(DateTimeFormatter.fromDateTime(from), offset, limit, DateTimeFormatter.toDateTime(to));
     }

     public Page<Book> getPageOfRecentBooks(int offset, int limit, String to) {
          return getPageOfRecentBooks(LocalDateTime.now().minus(1,ChronoUnit.CENTURIES), offset, limit, DateTimeFormatter.toDateTime(to));
     }

     public Page<Book> getPageOfRecentBooks(String from, int offset, int limit) {
          return getPageOfRecentBooks(DateTimeFormatter.fromDateTime(from), offset, limit, LocalDateTime.now());
     }

     public Page<Book> getPageOfRecentBooks(int offset, int limit) {
          return getPageOfRecentBooks(LocalDateTime.now().minus(1,ChronoUnit.CENTURIES), offset, limit, LocalDateTime.now());
     }

     public Page<Book> getPageOfRecentBooks() {
          return getPageOfRecentBooks(defaultOffset, defaultLimit);
     }

     public Page<Book> getPageOfPopularBooks(int offset, int limit) {
          Logger.getLogger(BookService.class.getName()).info("getPageOfPopularBooks started with offset " + offset + " and limit " + limit);
          Pageable nextPage = PageRequest.of(offset, limit);
          return bookRepository.findAllByPopularityValue(nextPage);
     }

     public Page<Book> getPageOfPopularBooks() {
          return getPageOfPopularBooks(defaultOffset, defaultLimit);
     }


     public Book getBookBySlug(String slug) {
          Logger.getLogger(BookService.class.getName()).info("getBookBySlug with slug: " + slug);
          return bookRepository.findBySlug(slug);
     }

     public Book getBookById(int id) {
          Logger.getLogger(BookService.class.getName()).info("getBookById with id: " + id);
          return bookRepository.findById(id).get();
     }

     public Page<Book> getPageOfSearchResultBooks(String query, int offset, int limit) {
          Logger.getLogger(BookService.class.getName()).info("find <<" + query + ">> with offset " + offset + " and limit " + limit);
          Pageable nextPage = PageRequest.of(offset, limit);
          return bookRepository.findBooksByTitleContaining(query, nextPage);
     }

     public int getCountOfSearchResult(String query) {
          return bookRepository.countByTitleContaining(query);
     }
}
