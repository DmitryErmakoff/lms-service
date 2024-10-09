package ru.d3m4k.lms.service.dto;

import lombok.Data;


@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
