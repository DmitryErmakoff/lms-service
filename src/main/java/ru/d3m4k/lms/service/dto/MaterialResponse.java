package ru.d3m4k.lms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {
    private Long id;
    private String title;
    private String description;
    private Long fileId;
    private Long teacherId;
    private LocalDateTime createdAt;
}
