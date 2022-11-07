package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.main.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findTagBySlug(String slug);
}
