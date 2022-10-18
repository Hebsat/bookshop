package com.example.MyBookShopApp.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class SpringfoxConfig {

    public static final String TAG_1 = "Books data controller";

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.ant("/api/books/**"))
                .build()
                .tags(new Tag(TAG_1, "Methods for getting books data"))
                .apiInfo(apiInfo());
    }

    public ApiInfo apiInfo() {
        return new ApiInfo(
                "Bookshop API",
                "API for bookstore",
                "1.0",
                "http://termsofservice.org",
                new Contact("API owner", "http://ownersite.com", "owner@mail.org"),
                "bookshop_api_licence",
                "http://www.license.edu.org",
                new ArrayList<>()
        );
    }
}
