package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.api.ApiSimpleResponse;
import com.example.MyBookShopApp.api.ContactConfirmationPayload;
import com.example.MyBookShopApp.api.RegistrationForm;
import com.example.MyBookShopApp.api.UserDto;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repositories.UserRepository;
import com.example.MyBookShopApp.security.BookStoreUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public boolean registerNewUser(RegistrationForm registrationForm) {
        if (userRepository.findUserByEmail(registrationForm.getEmail()).isEmpty()) {
            User user = new User();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            user.setHash(registrationForm.getEmail());
            user.setRegTime(LocalDateTime.now());
            user.setBalance(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public ApiSimpleResponse login(ContactConfirmationPayload contactConfirmationPayload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                contactConfirmationPayload.getContact(), contactConfirmationPayload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ApiSimpleResponse(true);
    }

    public UserDto getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return UserDto.builder().build();
        }
        BookStoreUserDetails userDetails = (BookStoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByEmail(userDetails.getUsername()).orElseThrow();
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .balance(user.getBalance())
                .isAuth(true)
                .build();
    }
}
