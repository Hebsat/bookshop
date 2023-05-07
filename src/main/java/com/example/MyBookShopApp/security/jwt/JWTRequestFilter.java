package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.security.BookStoreUserDetails;
import com.example.MyBookShopApp.security.BookstoreUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    try {
                        username = jwtUtil.extractUsername(token);
                    }
                    catch (ExpiredJwtException ignored) {}
                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    BookStoreUserDetails bookStoreUserDetails = (BookStoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(token, bookStoreUserDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                bookStoreUserDetails, null, bookStoreUserDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
