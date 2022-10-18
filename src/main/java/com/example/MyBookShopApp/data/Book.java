package com.example.MyBookShopApp.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "books", indexes = {@Index(name = "books_slug", columnList = "slug", unique = true)})
@Data
@ApiModel(description = "entity representing a book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "books id generated by db", position = 1)
    private int id;
    @Column(name = "pub_date", nullable = false, columnDefinition = "TIMESTAMP")
    @ApiModelProperty(value = "date of book publication", position = 5)
    private LocalDateTime publicationDate;
    @Column(name = "is_bestseller", columnDefinition = "SMALLINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ApiModelProperty(value = "if isBestseller = 1, the book is considered to be bestseller and if 0, then book is not a bestseller", position = 6)
    private boolean isBestseller;
    @Column(nullable = false)
    @ApiModelProperty(value = "mnemonic identity sequence of characters", position = 8)
    private String slug;
    @Column(nullable = false)
    @ApiModelProperty(value = "book title", position = 2)
    private String title;
    @ApiModelProperty(value = "image url", position = 7)
    private String image;
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "book description text", position = 9)
    private String description;
    @Column(nullable = false)
    @ApiModelProperty(value = "book price without discount", position = 3)
    private Double price;
    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    @ApiModelProperty(value = "discount value for book", position = 4)
    private int discount;

    @ManyToMany(mappedBy = "bookList")
    @JsonIgnore
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
