package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.Tag;
import com.example.MyBookShopApp.data.genre.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findBySlug(String slug);

    Page<Book> findAllByPublicationDateBetweenOrderByPublicationDateDesc(LocalDateTime after, LocalDateTime before, Pageable nextPage);

//    @Query("FROM Book WHERE isBestseller = true ORDER BY publicationDate DESC")
    @Query(value = "SELECT * FROM books WHERE is_bestseller = 1", countQuery = "SELECT count(*) FROM books", nativeQuery = true)
    Page<Book> findByBestsellerIsTrue(Pageable nextPage);

    @Query(value = "SELECT * FROM books b JOIN books_popularity p ON b.id = p.book_id JOIN book2genre g ON b.id = g.book_id " +
            "ORDER BY (p.count_purchases + p.count_in_cart * 0.7 + p.count_postponed * 0.4) DESC",
            countQuery = "SELECT count(*) FROM books", nativeQuery = true)
    Page<Book> findAllByPopularityValue(Pageable pageable);

    Page<Book> findBooksByTitleContaining(String title, Pageable nextPage);

    int countByTitleContaining(String title);

    Page<Book> findBooksByTagListContains(Tag tag, Pageable pageable);

    Page<Book> findBooksByAuthorListContains(Author author, Pageable pageable);

    Page<Book> findBooksByGenre(Genre genre, Pageable pageable);

    Page<Book> findBooksByGenreIn(List<Genre> genres, Pageable pageable);
}
