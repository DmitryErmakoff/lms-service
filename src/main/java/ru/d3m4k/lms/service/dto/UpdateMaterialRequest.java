package ru.d3m4k.lms.service.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateMaterialRequest {
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private Long fileId;
}
