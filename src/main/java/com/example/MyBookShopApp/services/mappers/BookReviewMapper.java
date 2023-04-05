package com.example.MyBookShopApp.services.mappers;

import com.example.MyBookShopApp.api.BookReviewDto;
import com.example.MyBookShopApp.data.book.review.BookReview;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookReviewMapper {

    public BookReviewDto convertBookReviewToDto(BookReview bookRating) {
        int likes = (int) bookRating.getLikes().stream().filter(bookReviewLike -> bookReviewLike.getValue() > 0).count();
        int dislikes = (int) bookRating.getLikes().stream().filter(bookReviewLike -> bookReviewLike.getValue() < 0).count();
        return BookReviewDto.builder()
                .id(bookRating.getId())
                .text(bookRating.getText())
                .time(bookRating.getTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm")))
                .userName(bookRating.getUser().getName())
                .likes(likes)
                .dislikes(dislikes)
                .stars(getRatingStars(likes, dislikes))
                .build();
    }

    private List<Boolean> getRatingStars(int likes, int dislikes) {
        int starsCount = 0;
        if (likes + dislikes > 0) {
            int percentLikes = likes * 100 / (likes + dislikes);
            if (percentLikes > 80) starsCount = 5;
            else if (percentLikes > 60) starsCount = 4;
            else if (percentLikes > 40) starsCount = 3;
            else if (percentLikes > 20) starsCount = 2;
            else starsCount = 1;
        }
        List<Boolean> stars = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            stars.add(i < starsCount);
        }
        return stars;
    }
}
