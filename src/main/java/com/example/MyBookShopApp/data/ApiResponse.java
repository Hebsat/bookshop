package com.example.MyBookShopApp.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
public class ApiResponse<T> {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timeStamp;
    private String message;
    private String debugMessage;
    private int count;
    private Collection<T> data;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(HttpStatus status, String message, Collection<T> data) {
        this();
        this.status = status;
        this.message = message;
        this.data = data;
        this.count = data.size();
    }

    public ApiResponse(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getMessage();
    }
}
