package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

     private final JdbcTemplate jdbcTemplate;

     @Autowired
     public BookService(JdbcTemplate jdbcTemplate) {
          this.jdbcTemplate = jdbcTemplate;
     }

     public List<Book> getBookData() {
          List<Book> books = jdbcTemplate.query(
                  "SELECT books.id, authors.first_name author, title, priceOld, price FROM books JOIN authors ON books.author_id = authors.id",
                  (ResultSet rs, int rowNum) -> {
               Book book = new Book();
               book.setId(rs.getInt("id"));
               book.setAuthor(rs.getString("author"));
               book.setTitle(rs.getString("title"));
               book.setPriceOld(rs.getString("priceOld"));
               book.setPrice(rs.getString("price"));
               return book;
          });
          return new ArrayList<>(books);
     }
}
