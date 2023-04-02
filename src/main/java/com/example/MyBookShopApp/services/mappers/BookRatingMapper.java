package com.example.MyBookShopApp.services.mappers;

import com.example.MyBookShopApp.api.BookRatingDto;
import com.example.MyBookShopApp.data.book.BookRating;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repositories.BookRatingRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRatingMapper {

    private final UserRepository userRepository;
    private final BookRatingRepository bookRatingRepository;

    public BookRatingDto getBookRatingDto(Book book) {
        int starsCount = getStarsCount(book.getRatings());
        return BookRatingDto.builder()
                .starsCount(starsCount)
                .totalRatings(book.getRatings().size())
                .stars(getRatingStars(starsCount))
                .starsValues(getStarsValues(book.getRatings()))
                .myStars(getMyStars(book))
                .build();
    }

    private int getStarsCount(List<BookRating> ratings) {
        return ratings.isEmpty() ? 0 : (int) Math.round((double) (ratings.stream().mapToInt(BookRating::getRating).sum() / ratings.size()));
    }

    private List<Boolean> getRatingStars(int starsCount) {
        List<Boolean> stars = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            stars.add(i < starsCount);
        }
        return stars;
    }

    private List<Integer> getStarsValues(List<BookRating> ratings) {
        List<Integer> values = new ArrayList<>(5);
        for (int i = 5; i > 0; i--) {
            int current = i;
            values.add((int) ratings.stream().filter(bookRating -> bookRating.getRating() == current).count());
        }
        return values;
    }

    private List<Boolean> getMyStars(Book book) {
        User user = userRepository.findById(1).orElse(null);
        if (user == null) return getRatingStars(0);
        BookRating bookRating = bookRatingRepository.findBookRatingByBookAndUser(book, user).orElse(new BookRating());
        return getRatingStars(bookRating.getRating());
    }
}
