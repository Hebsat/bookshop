package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.book.review.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {
}
