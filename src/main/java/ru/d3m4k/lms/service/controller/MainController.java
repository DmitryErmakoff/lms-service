package ru.d3m4k.lms.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.d3m4k.lms.service.dto.UserDto;
import ru.d3m4k.lms.service.util.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/unsecured")
    public String unsecured() {
        return "unsecured data";
    }

    @GetMapping("/secured")
    public String securedData() {
        return "secured data";
    }

    @GetMapping("/admin")
    public String adminData() {
        return "admin data";
    }

    @GetMapping("/info")
    public UserDto userData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return UserDto.builder()
                .login(userDetails.getUsername())
                .email(userDetails.getEmail())
                .createdAt(userDetails.getCreatedAt())
                .build();
    }
}
