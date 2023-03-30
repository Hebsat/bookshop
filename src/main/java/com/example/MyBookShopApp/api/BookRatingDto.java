package com.example.MyBookShopApp.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BookRatingDto {

    private int starsCount;
    private int totalRatings;
    private List<Boolean> stars;
    private List<Integer> starsValues;
    private List<Boolean> myStars;
}
