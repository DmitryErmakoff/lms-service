package ru.d3m4k.lms.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentsFromGroupResponseDto {
    private Long id;
    private String name;
    private List<UserDto> users;
}
