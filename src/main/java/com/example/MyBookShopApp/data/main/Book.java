package com.example.MyBookShopApp.data.main;

import com.example.MyBookShopApp.data.book.BookRating;
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
    @Column(name = "is_bestseller", columnDefinition = "SMALLINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean bestseller;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String title;
    private String image;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    private int discount;
    @ManyToMany(mappedBy = "bookList")
    List<Author> authorList;
    @ManyToMany(mappedBy = "bookList")
    List<Tag> tagList;
    @ManyToOne
    @JoinTable(name = "book2genre", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Genre genre;
    @OneToMany(mappedBy = "book")
    private List<BookFile> bookFileList;
    @OneToMany(mappedBy = "book")
    private List<BookRating> ratings;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", publicationDate=" + publicationDate +
                ", isBestseller=" + bestseller +
                ", title='" + title +
                "', price='" + price +
                "', discount=" + discount +
                ", authorList=" + Arrays.toString(authorList.stream().map(Author::getName).toArray()) +
                ", tagList=" + Arrays.toString(tagList.stream().map(Tag::getName).toArray()) +
                ", genre=" + genre.getName() +
                '}';
    }
}
