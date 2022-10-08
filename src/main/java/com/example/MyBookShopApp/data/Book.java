package com.example.MyBookShopApp.data;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "books", indexes = {@Index(name = "books_slug", columnList = "slug", unique = true)})
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isBestseller() {
        return isBestseller;
    }

    public void setBestseller(boolean bestseller) {
        isBestseller = bestseller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }
}
