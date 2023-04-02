package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.book.BookRating;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    Optional<BookRating> findBookRatingByBookAndUser(Book book, User user);
}
