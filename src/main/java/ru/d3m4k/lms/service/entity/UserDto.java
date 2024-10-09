package ru.d3m4k.lms.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
}
