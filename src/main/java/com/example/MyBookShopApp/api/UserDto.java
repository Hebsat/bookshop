package com.example.MyBookShopApp.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDto {

    private boolean isAuth;
    private String name;
    private String email;
    private String phone;
    private double balance;
}
