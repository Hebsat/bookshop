package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.main.Book;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class CookieService {

    private final BookService bookService;

    public CookieService(BookService bookService) {
        this.bookService = bookService;
    }

    public boolean validateCookie(String cookie) {
        return cookie != null && !cookie.isEmpty();
    }

    private String writeValue(String content, String value) {
        List<String> data = new ArrayList<>(readCookie(content));
        if (data.stream().noneMatch(s -> s.equals(value))) {
            data.add(value);
            return String.join("/", data);
        }
        return content;
    }

    private List<String> readCookie(String content) {
        return List.of(content.split("/"));
    }

    public List<Book> getBooksFromCookie(String cookie) {
        return bookService.getBooksBySlugList(readCookie(cookie));
    }

    public List<Cookie> addContentToCookie(
            String thisCookieContent, String anotherCookieContent, String contentToAdd, String thisCookieName, String anotherCookieName, String path) {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(validateCookie(anotherCookieContent) ?
                removeContentFromCookie(anotherCookieContent, contentToAdd, anotherCookieName, "/") :
                createCookie(anotherCookieName, "", path));
        cookies.add(createCookie(thisCookieName,
                validateCookie(thisCookieContent) ? writeValue(thisCookieContent, contentToAdd) : contentToAdd, path));
        return cookies;
    }

    private Cookie createCookie(String name, String content, String path) {
        Cookie cookie = new Cookie(name, content);
        cookie.setPath(path);
        return cookie;
    }

    public Cookie removeContentFromCookie(String cookieContent, String contentToRemove, String cookieName, String path){
        List<String> content = new ArrayList<>(readCookie(cookieContent));
        content.remove(contentToRemove);
        return createCookie(cookieName, String.join("/", content), path);
    }

    public int getCountOfBooksInCookie(String cookieContent) {
        return validateCookie(cookieContent) ? readCookie(cookieContent).size() : 0;
    }

    public List<String> getRequestValues(HttpServletRequest request, String cartContent, String postponedContent) {
        List<String> result = new ArrayList<>();
        String uri = Path.of(request.getRequestURI()).getName(1).toString();
        result.add(uri);
        switch (uri) {
            case ("cart"): {
                result.add(cartContent);
                result.add("cartContents");
                result.add(postponedContent);
                result.add("postponedContents");
                break;
            }
            case ("postponed"): {
                result.add(postponedContent);
                result.add("postponedContents");
                result.add(cartContent);
                result.add("cartContents");
                break;
            }
        }
        return result;
    }
}
