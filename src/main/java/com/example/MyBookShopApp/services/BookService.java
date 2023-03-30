package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.data.book.BookRating;
import com.example.MyBookShopApp.data.main.Book;
import com.example.MyBookShopApp.data.user.UserEntity;
import com.example.MyBookShopApp.errors.WrongResultException;
import com.example.MyBookShopApp.services.mappers.BookMapper;
import com.example.MyBookShopApp.repositories.BookRatingRepository;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.services.util.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

     @Value("${bookshop.default.page}")
     private int defaultPage;
     @Value("${bookshop.default.size}")
     private int defaultSize;

     private final BookRepository bookRepository;
     private final UserRepository userRepository;
     private final BookRatingRepository bookRatingRepository;
     private final BookMapper bookMapper;

     public BooksListDto getPageOfRecommendedBooks(int page, int size) {
          Logger.getLogger(BookService.class.getName()).info("getPageOfRecommendedBooks started with page " + page + " and size " + size);
          Pageable nextPage = PageRequest.of(page, size, Sort.by(
                  Sort.Direction.DESC, "discount")
                  .and(Sort.by(Sort.Direction.ASC, "title")));
          Page<Book> books = bookRepository.findAll(nextPage);
          return BooksListDto.builder()
                  .totalPages(books.getTotalPages())
                  .count((int) books.getTotalElements())
                  .books(books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                  .build();
     }

     public BooksListDto getPageOfRecommendedBooks() {
          return getPageOfRecommendedBooks(defaultPage, defaultSize);
     }

     private BooksListDto getPageOfRecentBooks(LocalDateTime from, int page, int size, LocalDateTime to) {
          Logger.getLogger(BookService.class.getName()).info(
                  "getPageOfRecentBooks started from " + from + " to " + to + " with page " + page + " and size " + size);
          Pageable nextPage = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publicationDate"));
          Page<Book> bookPage = bookRepository.findAllByPublicationDateBetweenOrderByPublicationDateDesc(from, to, nextPage);
          return BooksListDto.builder()
                  .totalPages(bookPage.getTotalPages())
                  .count((int) bookPage.getTotalElements())
                  .books(bookPage.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                  .build();
     }

     public BooksListDto getPageOfRecentBooks(String from, int page, int size, String to) {
          LocalDateTime fromTime = from == null ? getFromTime() : DateTimeFormatter.fromDateTime(from);
          LocalDateTime toTime = to == null ? getToTime() : DateTimeFormatter.fromDateTime(to);
          return getPageOfRecentBooks(fromTime, page, size, toTime);
     }

     public BooksListDto getPageOfRecentBooks() {
          return getPageOfRecentBooks(getFromTime(), defaultPage, defaultSize, getToTime());
     }

     public BooksListDto getPageOfPopularBooks(int page, int size) {
          Logger.getLogger(BookService.class.getName()).info("getPageOfPopularBooks started with page " + page + " and size " + size);
          Pageable nextPage = PageRequest.of(page, size);
          Page<Book> books = bookRepository.findAllByPopularityValue(nextPage);
          return BooksListDto.builder()
                  .totalPages(books.getTotalPages())
                  .count((int) books.getTotalElements())
                  .books(books.getContent().stream().map(bookMapper::convertBookToBookDtoLight).collect(Collectors.toList()))
                  .build();
     }

     public BooksListDto getPageOfPopularBooks() {
          return getPageOfPopularBooks(defaultPage, defaultSize);
     }

     public String getBookBySlug(String slug, Model model) {
          Logger.getLogger(BookService.class.getName()).info("getBookBySlug with slug: " + slug);
          Optional<Book> optionalBook = bookRepository.findBySlug(slug);
          model.addAttribute("topBarIdentifier", "genres");
          model.addAttribute("pageTitle", "book");
          if (optionalBook.isEmpty()) {
               model.addAttribute("error", "Книги с идентификатором " + slug + " не существует");
               return "/errors/404";
          }
          Book book = optionalBook.get();
          model.addAttribute("pageTitlePart", book.getTitle());
          model.addAttribute("pageHeadDescription",
                  book.getTitle() + " Описание: " + book.getDescription().substring(0, 100) + "...");
          model.addAttribute("book", bookMapper.convertBookToBookDtoFull(book));
          return "/books/slug";
     }

     public ApiSimpleResponse rateBook(String slug, int rating) throws WrongResultException {
          Logger.getLogger(BookService.class.getName()).info("rateBook with slug: " + slug + " on " + rating);
          UserEntity user = userRepository.findById(1).orElseThrow(() -> new WrongResultException("User with id are not found"));
          Book book = bookRepository.findBySlug(slug).orElseThrow(() -> new WrongResultException("Book with slug " + slug + " are not found"));
          BookRating bookRating = bookRatingRepository.findBookRatingByBookAndUser(book, user).orElse(new BookRating(book, user));
          bookRating.setRating(rating);
          bookRatingRepository.save(bookRating);
          return new ApiSimpleResponse(true);
     }

     private LocalDateTime getFromTime() {
          return LocalDateTime.now().minus(1,ChronoUnit.CENTURIES);
     }

     private LocalDateTime getToTime() {
          return LocalDateTime.now();
     }
}
