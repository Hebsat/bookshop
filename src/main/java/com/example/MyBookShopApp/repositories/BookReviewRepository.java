package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Integer> {

    Optional<BookReview> findBookReviewByBookAndUser(Book book, User user);
}
