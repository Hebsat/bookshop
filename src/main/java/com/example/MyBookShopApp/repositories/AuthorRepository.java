package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.main.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findAuthorBySlug(String slug);
}
