package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksListDto;
import com.example.MyBookShopApp.errors.BookshopWrongParameterException;
import com.example.MyBookShopApp.errors.EmptySearchQueryException;
import com.example.MyBookShopApp.errors.WrongEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(BookshopWrongParameterException.class)
    public ResponseEntity<BooksListDto> handleBookshopWrongParameterException(Exception e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BooksListDto(e.getMessage()));
    }

    @ExceptionHandler(EmptySearchQueryException.class)
    public String handleEmptySearchQueryException(Exception e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/search/empty";
    }

    @ExceptionHandler(WrongEntityException.class)
    public String handleWrongEntityException(Exception e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("error", e);
        return "redirect:/errors/404";
    }
}
