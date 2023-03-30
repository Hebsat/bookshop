package com.example.MyBookShopApp.services.mappers;

import com.example.MyBookShopApp.api.BookDto;
import com.example.MyBookShopApp.data.main.Author;
import com.example.MyBookShopApp.data.main.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMapper {

    private final TagMapper tagMapper;
    private final BookFileMapper bookFileMapper;
    private final BookRatingMapper bookRatingMapper;

    public BookDto convertBookToBookDtoLight(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authors(getAuthorsShort(book.getAuthorList()))
                .price(book.getPrice())
                .bestseller(book.isBestseller())
                .discount(book.getDiscount())
                .slug(book.getSlug())
                .image(book.getImage())
                .discountPrice(getDiscountPrice(book.getPrice(), book.getDiscount()))
                .build();
    }

    private String getAuthorsShort(List<Author> authorList) {
        authorList.sort(Comparator.comparingInt(author -> author.getBookList().size()));
        Collections.reverse(authorList);
        return authorList.stream().findFirst().get().getName() + (authorList.size() > 1 ?  " и другие" : "");
    }

    private String getDiscountPrice(double price, int discount) {
        return new DecimalFormat("#.00").format(price - price * discount / 100);
    }

    public BookDto convertBookToBookDtoFull(Book book) {
        BookDto bookDto = convertBookToBookDtoLight(book);
        bookDto.setAuthorList(book.getAuthorList());
        bookDto.setDescription(book.getDescription());
        bookDto.setTagList(book.getTagList().stream().map(tagMapper::convertTagToTagDto).collect(Collectors.toList()));
        bookDto.setBookFileList(book.getBookFileList().stream().map(bookFileMapper::convertBookFileToBookFileDto).collect(Collectors.toList()));
        bookDto.setRatings(bookRatingMapper.getBookRatingDto(book));
        return bookDto;
    }
}