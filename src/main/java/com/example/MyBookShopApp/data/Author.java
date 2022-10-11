package com.example.MyBookShopApp.data;

import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "authors", indexes = {@Index(name = "authors_slug", columnList = "slug", unique = true)})
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String photo;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(name = "book2author", joinColumns = @JoinColumn(name = "author_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> bookList;

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
//                ", photo='" + photo + '\'' +
//                ", slug='" + slug + '\'' +
                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
                ", bookList=" + Arrays.toString(bookList.stream().map(Book::getTitle).toArray()) +
                '}';
    }
}
