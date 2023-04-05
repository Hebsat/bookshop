package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.data.book.review.BookReviewLike;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.errors.WrongResultException;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.BookReviewLikeRepository;
import com.example.MyBookShopApp.repositories.BookReviewRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final UserRepository userRepository;

    public ApiSimpleResponse setLikeReview(Integer reviewId, Short likeValue) throws BookshopWrongParameterException {
        User user = userRepository.findById(1).orElseThrow(() -> new BookshopWrongParameterException("Пользователь не существует"));
        BookReview bookReview = bookReviewRepository.findById(reviewId).orElseThrow(() -> new BookshopWrongParameterException("Ревью не существует"));
        Optional<BookReviewLike> likeOptional = bookReview.getLikes().stream().filter(bookReviewLike -> bookReviewLike.getUser().getId() == user.getId()).findFirst();
        BookReviewLike like;
        if (likeOptional.isPresent()) {
            like = likeOptional.get();
            if (like.getValue() == likeValue) {
                bookReviewLikeRepository.delete(like);
                return new ApiSimpleResponse(false);
            }
        }
        else {
            like = new BookReviewLike();
            like.setReview(bookReview);
            like.setUser(user);
        }
        like.setValue(likeValue);
        like.setTime(LocalDateTime.now());
        bookReviewLikeRepository.save(like);
        return new ApiSimpleResponse(true);
    }

    public ApiSimpleResponse setReview(String slug, String text) throws BookshopWrongParameterException, WrongResultException {
        if (text.length() < 30) throw new WrongResultException("Отзыв слишком короткий. Напишите, пожалуйста, более развернутый отзыв");
        User user = userRepository.findById(1).orElseThrow(() -> new BookshopWrongParameterException("Пользователь не существует"));
        Book book = bookRepository.findBySlug(slug).orElseThrow(() -> new BookshopWrongParameterException("Книга не существует"));
        Optional<BookReview> optionalBookReview = bookReviewRepository.findBookReviewByBookAndUser(book, user);
        BookReview bookReview;
        if (optionalBookReview.isPresent()) {
            bookReview = optionalBookReview.get();
        }
        else {
            bookReview = new BookReview();
            bookReview.setUser(user);
            bookReview.setBook(book);
        }
        bookReview.setText(text);
        bookReview.setTime(LocalDateTime.now());
        bookReviewRepository.save(bookReview);
        return new ApiSimpleResponse(true);
    }
}
