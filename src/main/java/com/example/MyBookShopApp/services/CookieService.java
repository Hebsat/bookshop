package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.BookDto;
import com.example.MyBookShopApp.api.CookieContentDto;
import com.example.MyBookShopApp.errors.SomethingWrongException;
import com.example.MyBookShopApp.repositories.BookRepository;
import com.example.MyBookShopApp.services.mappers.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CookieService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public int getCountOfBooksInCookie(String cookieContent) {
        log.debug("getCountOfBooksInCookie " + cookieContent);
        return validateCookie(cookieContent) ? readCookie(cookieContent).size() : 0;
    }

    public String getCartAndPostponedMainPage(HttpServletRequest request, String cartContent, String postponedContent, Model model) throws SomethingWrongException {
        CookieContentDto cookie = getRequestValues(request, cartContent, postponedContent);
        model.addAttribute("pageTitle", cookie.getType());
        if (validateCookie(cookie.getContent())) {
            List<BookDto> booksFromCookie = getBooksFromCookie(cookie.getContent());
            model.addAttribute("books", booksFromCookie);
            model.addAttribute("priceOld", booksFromCookie.stream().mapToDouble(BookDto::getPrice).sum());
            model.addAttribute("price", booksFromCookie.stream()
                    .mapToDouble(book -> book.getPrice() - (book.getPrice() * book.getDiscount() / 100)).sum());
            model.addAttribute("allBooks", String.join(", ", readCookie(cookie.getContent())));
        }
        return cookie.getType();
    }

    public ApiSimpleResponse changeBookStatus(List<String> bookSlugs, String status, HttpServletResponse response, String cartContent, String postponedContent) {
        switch (status) {
            case "CART" : return addContentToCookie(cartContent, postponedContent, bookSlugs, "cartContents", "postponedContents", "/", response);
            case "KEPT" : return addContentToCookie(postponedContent, cartContent, bookSlugs, "postponedContents", "cartContents", "/", response);
            case "UNLINK" : return removeContentFromCookies(cartContent, postponedContent, bookSlugs, response);
            default: return new ApiSimpleResponse(false);
        }
    }

    private boolean validateCookie(String cookie) {
        return cookie != null && !cookie.isEmpty();
    }

    private String writeValue(String content, List<String> values) {
        List<String> data = new ArrayList<>(readCookie(content));
        values.forEach(value -> {
            if (!data.contains(value)) {
                data.add(value);
            }
        });
        return String.join("/", data);
    }

    private List<String> readCookie(String content) {
        return validateCookie(content) ? List.of(content.split("/")) : new ArrayList<>();
    }

    private List<BookDto> getBooksFromCookie(String cookie) {
        return bookRepository.findBooksBySlugIn(readCookie(cookie))
                .stream().map(bookMapper::convertBookToBookDtoFull).collect(Collectors.toList());
    }

    private ApiSimpleResponse addContentToCookie(String thisCookieContent, String anotherCookieContent, List<String> contentToAdd,
                                    String thisCookieName, String anotherCookieName, String path, HttpServletResponse response) {
        response.addCookie(validateCookie(anotherCookieContent) ?
                removeContentFromCookie(anotherCookieContent, contentToAdd, anotherCookieName, path) :
                createCookie(anotherCookieName, "", path));
        response.addCookie(createCookie(thisCookieName,
                writeValue(validateCookie(thisCookieContent) ? thisCookieContent : "", contentToAdd), path));
        return new ApiSimpleResponse(true);
    }

    private Cookie createCookie(String name, String content, String path) {
        Cookie cookie = new Cookie(name, content);
        cookie.setPath(path);
        return cookie;
    }

    private ApiSimpleResponse removeContentFromCookies(String cartContents, String postponedContents, List<String> contentToRemove, HttpServletResponse response) {
        response.addCookie(removeContentFromCookie(cartContents, contentToRemove, "cartContents", "/"));
        response.addCookie(removeContentFromCookie(postponedContents, contentToRemove, "postponedContents", "/"));
        return new ApiSimpleResponse(true);
    }

    private Cookie removeContentFromCookie(String cookieContent, List<String> contentToRemove, String cookieName, String path){
        List<String> content = new ArrayList<>(readCookie(cookieContent));
        contentToRemove.forEach(content::remove);
        return createCookie(cookieName, String.join("/", content), path);
    }

    private CookieContentDto getRequestValues(HttpServletRequest request, String cartContent, String postponedContent) throws SomethingWrongException {
        String uri = Path.of(request.getRequestURI()).getName(1).toString();
        switch (uri) {
            case ("cart"): {
                return new CookieContentDto(uri, cartContent);
            }
            case ("postponed"): {
                return new CookieContentDto(uri, postponedContent);
            }
            default: throw new SomethingWrongException("Что-то пошло не так..");
        }
    }

    public ApiSimpleResponse addAllBooksFromPostponedToCart(HttpServletResponse response, String postponedCookieContent, String cartCookieContent) {
        log.info("addAllBooksFromPostponedToCart: " + postponedCookieContent);
        if (!validateCookie(postponedCookieContent)) postponedCookieContent = "";
        response.addCookie(createCookie("cartContents",
                validateCookie(cartCookieContent) ? writeValue(cartCookieContent, readCookie(postponedCookieContent)) : postponedCookieContent, "/"));
        response.addCookie(createCookie("postponedContents", "", "/"));
        return new ApiSimpleResponse(true);
    }
}
