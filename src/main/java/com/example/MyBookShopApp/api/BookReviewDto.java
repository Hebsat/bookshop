package com.example.MyBookShopApp.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BookReviewDto {

    private int id;
    private String userName;
    private String time;
    private String text;
    private int likes;
    private int dislikes;
    private List<Boolean> stars;
}
