package ru.d3m4k.lms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private Long id;
    private String fileName;
    private Long uploadedBy;
    private LocalDateTime createdAt;
}
