package ru.d3m4k.lms.service.dto;

import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {
    private Long id;
    private String firstname;
    private String surname;
    private String middleName;
    private String login;
    private String email;
    private LocalDateTime createdAt;
    @Null
    private List<String> roles;
    private GroupResponseDto group;
}
