package com.example.MyBookShopApp.api;

import lombok.Data;

@Data
public class RegistrationForm {

    private String name;
    private String email;
    private String phone;
    private String password;
}
