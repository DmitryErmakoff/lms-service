package ru.d3m4k.lms.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.d3m4k.lms.service.dto.GroupResponseDto;
import ru.d3m4k.lms.service.dto.UserDto;
import ru.d3m4k.lms.service.dto.UserInfoDto;
import ru.d3m4k.lms.service.entity.Group;
import ru.d3m4k.lms.service.entity.User;
import ru.d3m4k.lms.service.service.UserService;
import ru.d3m4k.lms.service.util.CustomUserDetails;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

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

    // TODO отрефакторить
    @GetMapping("/info")
    public UserInfoDto userData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Group group = user.getGroup();
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(userDetails.getId())
                .firstname(userDetails.getFirstName())
                .surname(userDetails.getSurname())
                .middleName(userDetails.getMiddleName())
                .login(userDetails.getUsername())
                .email(userDetails.getEmail())
                .createdAt(userDetails.getCreatedAt())
                .surname(userDetails.getSurname())
                .roles(
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .build();
        if (group != null) {
            GroupResponseDto groupResponseDto = GroupResponseDto.builder()
                    .id(user.getGroup().getId())
                    .name(user.getGroup().getName())
                    .createdAt(user.getGroup().getCreatedAt())
                    .build();
            userInfoDto.setGroup(groupResponseDto);
        }
        return userInfoDto;
    }
}
