package com.example.MyBookShopApp.data;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "books", indexes = {@Index(name = "books_slug", columnList = "slug", unique = true)})
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "pub_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime publicationDate;
    @Column(columnDefinition = "SMALLINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isBestseller;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String title;
    private String image;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int discount;

    @ManyToMany(mappedBy = "bookList")
    List<Author> authorList;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", publicationDate=" + publicationDate +
                ", isBestseller=" + isBestseller +
//                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
//                ", image='" + image + '\'' +
//                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", discount=" + discount +
                ", authorList=" + Arrays.toString(authorList.stream().map(Author::getName).toArray()) +
                '}';
    }
}
