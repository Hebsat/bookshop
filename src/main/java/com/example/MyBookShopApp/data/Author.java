package com.example.MyBookShopApp.data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "authors", indexes = {@Index(name = "authors_slug", columnList = "slug", unique = true)})
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
