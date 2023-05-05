package com.example.MyBookShopApp.errors;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.BooksListDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.StringJoiner;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(BookshopWrongParameterException.class)
    public ResponseEntity<BooksListDto> handleBookshopWrongParameterException(Exception e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BooksListDto.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiSimpleResponse handleConstraintViolationException(ConstraintViolationException e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
        StringJoiner message = new StringJoiner("; ");
        e.getConstraintViolations().forEach(constraintViolation -> message.add(constraintViolation.getMessage()));
        return new ApiSimpleResponse(false, message.toString());
    }

    @ExceptionHandler(WrongResultException.class)
    public ResponseEntity<ApiSimpleResponse> handleWrongResultException(Exception e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiSimpleResponse(false, e.getMessage()));
    }

    @ExceptionHandler(SomethingWrongException.class)
    public String handleSomethingWrongException(Exception e) {
        Logger.getLogger(this.getClass().getName()).warning(e.getLocalizedMessage());
        return "redirect:/errors/404";
    }
}
