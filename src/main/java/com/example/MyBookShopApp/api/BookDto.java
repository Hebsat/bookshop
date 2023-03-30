package com.example.MyBookShopApp.api;

import com.example.MyBookShopApp.data.main.Author;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ApiModel(description = "entity representing a book")
@JsonPropertyOrder({"id", "title", "authors", "price", "publicationDate", "bestseller", "discount", "slug", "image", "discountPrice"})
public class BookDto {

    @ApiModelProperty(value = "books id generated by db")
    private int id;
    @ApiModelProperty(value = "book's author. If authors are more then 1, then name first author")
    private String authors;
    @ApiModelProperty(value = "date of book publication")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime publicationDate;
    @ApiModelProperty(value = "if isBestseller = 1, the book is considered to be bestseller and if 0, then book is not a bestseller", name = "isBestseller", position = 6)
    private boolean bestseller;
    @ApiModelProperty(value = "mnemonic identity sequence of characters")
    private String slug;
    @ApiModelProperty(value = "book title")
    private String title;
    @ApiModelProperty(value = "image url")
    private String image;
//    @ApiModelProperty(value = "book description text")
    @JsonIgnore
    private String description;
    @ApiModelProperty(value = "book price without discount")
    private Double price;
    @ApiModelProperty(value = "discount value for book")
    private int discount;
    @ApiModelProperty(value = "book price with discount")
    private String discountPrice;
    @JsonIgnore
    List<Author> authorList;
    @JsonIgnore
    List<TagDto> tagList;
    @JsonIgnore
    private List<BookFileDto> bookFileList;
    @JsonIgnore
    private BookRatingDto ratings;

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", authors='" + authors + '\'' +
                ", publicationDate=" + publicationDate +
                ", bestseller=" + bestseller +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", discountPrice='" + discountPrice + '\'' +
                ", authorList=" + authorList +
                ", tagList=" + tagList +
                ", bookFileList=" + bookFileList +
                ", ratings=" + ratings +
                '}';
    }
}