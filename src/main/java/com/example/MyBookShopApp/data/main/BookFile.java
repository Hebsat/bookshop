package com.example.MyBookShopApp.data.main;

import com.example.MyBookShopApp.data.book.file.BookFileTypeEntity;
import com.example.MyBookShopApp.data.enums.BookFileType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "book_files")
@Data
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String hash;
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private BookFileTypeEntity bookFileType;
    @Column(nullable = false)
    private String path;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
}
