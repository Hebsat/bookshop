package com.example.MyBookShopApp.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ApiSimpleResponse {

    private boolean result;
    private String error;

    public ApiSimpleResponse(boolean result) {
        this.result = result;
    }
}
