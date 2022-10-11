package com.example.MyBookShopApp.data.book.file;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "file_downloads")
@Data
public class FileDownloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private int bookId;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private int count;
}
