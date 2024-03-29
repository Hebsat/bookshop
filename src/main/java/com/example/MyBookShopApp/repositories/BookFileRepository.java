package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.data.main.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    Optional <BookFile> findBookFileByHash(String hash);
}
