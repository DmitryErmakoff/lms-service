package ru.d3m4k.lms.service.dto;

import lombok.Data;


@Data
public class RegistrationUserDto {
    private String firstName;
    private String surname;
    private String middleName;
    private String login;
    private String password;
    private String confirmPassword;
    private String email;
}
