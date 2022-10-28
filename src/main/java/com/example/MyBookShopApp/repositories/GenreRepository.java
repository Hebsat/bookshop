package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Genre findGenreBySlug(String slug);

    List<Genre> findGenresByParentGenreIsNull();
}
