package com.example.MyBookShopApp.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSimpleResponse {

    private boolean result;
    private String error;

    public ApiSimpleResponse(boolean result) {
        this.result = result;
    }
}
