package ru.d3m4k.lms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<GroupResponseDto> groups;
}
