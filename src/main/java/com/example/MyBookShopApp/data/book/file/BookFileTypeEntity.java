package com.example.MyBookShopApp.data.book.file;

import com.example.MyBookShopApp.data.enums.BookFileType;
import com.example.MyBookShopApp.data.main.BookFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book_file_types")
@Data
public class BookFileTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookFileType name;
    @Column(columnDefinition = "TEXT")
    private String description;

    public String getExtensionStringByType() {
        return BookFileType.getExtensionString(name);
    }
}
