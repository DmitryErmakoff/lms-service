package ru.d3m4k.lms.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineRequestDto {
    @NotBlank(message = "Название дисциплины обязательно")
    private String name;

    private String description;
}
