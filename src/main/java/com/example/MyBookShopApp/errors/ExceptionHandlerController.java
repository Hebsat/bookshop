package com.example.MyBookShopApp.errors;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.BooksListDto;
import com.example.MyBookShopApp.api.SearchQueryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(BookshopWrongParameterException.class)
    public ResponseEntity<BooksListDto> handleBookshopWrongParameterException(Exception e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BooksListDto.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(WrongResultException.class)
    public ResponseEntity<ApiSimpleResponse> handleWrongResultException(Exception e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiSimpleResponse(false));
    }

    @ExceptionHandler(SomethingWrongException.class)
    public String handleSomethingWrongException(Exception e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        return "redirect:/errors/404";
    }
}
