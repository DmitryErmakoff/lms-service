package ru.d3m4k.lms.service.dto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDto {
    Long id;
    String name;
    LocalDateTime createdAt;
}
