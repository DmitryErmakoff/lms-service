package ru.d3m4k.lms.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateRequestDto {
    @NotBlank(message = "Название группы обязательно")
    @Size(min = 2, max = 100, message = "Название группы должно быть от 2 до 100 символов")
    private String name;
}
