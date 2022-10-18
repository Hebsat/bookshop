package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findBySlug(String slug);

    List<Book> findAllByPublicationDateBetweenOrderByPublicationDateDesc(LocalDateTime after, LocalDateTime before);

    @Query("FROM Book WHERE isBestseller = true ORDER BY publicationDate DESC")
    List<Book> findAllByBestsellerIsTrue();
}
