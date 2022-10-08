package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.repositories.AuthorRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
          return bookRepository.findAll();
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
          return bookRepository.findAllByPublicationDateAfterOrderByPublicationDateDesc(LocalDateTime.of(2022,9,15,0,0));
     }

     public List<Book> getPopularBooks() {
          Logger.getLogger(BookService.class.getName()).info("getPopularBooks started");
          List<Book> bookList = bookRepository.findAllByBestsellerIsTrue();
          return bookList;
     }
}
