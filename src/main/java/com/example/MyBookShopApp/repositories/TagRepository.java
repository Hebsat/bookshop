package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findTagBySlug(String slug);
}
