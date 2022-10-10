package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BookService {

     private final BookRepository bookRepository;

     @Autowired
     public BookService(BookRepository bookRepository) {
          this.bookRepository = bookRepository;
     }

     public List<Book> getBookData() {
          Logger.getLogger(BookService.class.getName()).info("getBookData started");
          return bookRepository.findAll().subList(0, 20);
     }

     public Book getBookBySlug(String slug) {
          Logger.getLogger(BookService.class.getName()).info("getBookBySlug with slug: " + slug);
          return bookRepository.findBySlug(slug);
     }

     public Book getBook(int id) {
          Logger.getLogger(BookService.class.getName()).info("getBookById with id: " + id);
          return bookRepository.findById(id).get();
     }

     public List<Book> getRecentBooks() {
          Logger.getLogger(BookService.class.getName()).info("getRecentBooks started");
          List<Book> bookList = bookRepository
                  .findAllByPublicationDateAfterAndPublicationDateBeforeOrderByPublicationDateDesc(
                  LocalDateTime.of(2022,10,1,0,0,0,0).minus(7, ChronoUnit.DAYS),
                  LocalDateTime.of(2022,10,1,0,0,0,0)
          );
          return bookList.subList(0, Math.min(bookList.size(), 20));
     }

     public List<Book> getPopularBooks() {
          Logger.getLogger(BookService.class.getName()).info("getPopularBooks started");
          List<Book> bookList = bookRepository.findAllByBestsellerIsTrue();
          return bookList.subList(0, 20);
     }
}
