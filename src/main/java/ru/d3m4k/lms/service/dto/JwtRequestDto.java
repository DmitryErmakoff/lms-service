package ru.d3m4k.lms.service.dto;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String login;
    private String password;
}
