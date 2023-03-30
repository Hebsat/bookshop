package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.main.Genre;
import com.example.MyBookShopApp.data.main.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findBySlug(String slug);

    List<Book> findBooksBySlugIn(Collection<String> slugList);

    Page<Book> findAllByPublicationDateBetweenOrderByPublicationDateDesc(LocalDateTime after, LocalDateTime before, Pageable nextPage);

//    @Query(value = "SELECT * FROM books b JOIN books_popularity p ON b.id = p.book_id JOIN book2genre g ON b.id = g.book_id " +
//            "ORDER BY (p.count_purchases + p.count_in_cart * 0.7 + p.count_postponed * 0.4) DESC",
//            countQuery = "SELECT count(*) FROM books", nativeQuery = true)
//    Page<Book> findAllByPopularityValue(Pageable pageable);
    @Query(value = "SELECT b.*, g.* FROM books b LEFT JOIN book2user u ON u.book_id = b.id " +
            "LEFT JOIN book2user_type t ON u.type_id = t.id JOIN book2genre g ON b.id = g.book_id GROUP BY b.id, g.id " +
            "ORDER BY (COUNT(t.code = 'PAID') + COUNT(t.code = 'CART') * 0.7 + COUNT(t.code = 'KEPT') * 0.4) DESC",
             nativeQuery = true)
    Page<Book> findAllByPopularityValue(Pageable pageable);

    Page<Book> findBooksByTitleContaining(String title, Pageable nextPage);

    Page<Book> findBooksByTagListContains(Tag tag, Pageable pageable);

    Page<Book> findBooksByAuthorListContains(Author author, Pageable pageable);

    Page<Book> findBooksByGenreIn(Collection<Genre> genres, Pageable pageable);
}
