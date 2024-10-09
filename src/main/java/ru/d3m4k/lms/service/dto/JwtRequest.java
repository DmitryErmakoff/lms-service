package ru.d3m4k.lms.service.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
