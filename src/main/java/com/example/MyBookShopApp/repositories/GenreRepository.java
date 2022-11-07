package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.main.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findGenreBySlug(String slug);

    List<Genre> findGenresByParentGenreIsNull();
}
