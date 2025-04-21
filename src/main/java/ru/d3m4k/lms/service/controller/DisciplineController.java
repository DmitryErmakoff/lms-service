package ru.d3m4k.lms.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.d3m4k.lms.service.dto.DisciplineRequestDto;
import ru.d3m4k.lms.service.dto.DisciplineResponseDto;
import ru.d3m4k.lms.service.dto.GroupResponseDto;
import ru.d3m4k.lms.service.service.DisciplineService;
import ru.d3m4k.lms.service.util.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/disciplines")
@RequiredArgsConstructor
@Tag(name = "Disciplines Management", description = "Управление учебными дисциплинами")
public class DisciplineController {
    private final DisciplineService disciplineService;

    @Operation(
            summary = "Создать новую дисциплину",
            description = "Доступно преподавателям",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Дисциплина успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "409", description = "Дисциплина с таким именем уже существует")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DisciplineResponseDto createDiscipline(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DisciplineRequestDto dto) {
        return disciplineService.createDiscipline(userDetails.getId(), dto);
    }

    @Operation(
            summary = "Получить все дисциплины",
            description = "Доступно преподавателям дисциплины"
    )
    @ApiResponse(responseCode = "200", description = "Список всех дисциплин")
    @GetMapping
    public List<DisciplineResponseDto> getAllDisciplines() {
        return disciplineService.getAllDisciplines();
    }

    @Operation(
            summary = "Получить дисциплину по ID",
            description = "Доступно преподавателям дисциплины"
    )
    @Parameter(name = "disciplineId", description = "ID дисциплины", example = "5")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о дисциплине"),
            @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
    })
    @GetMapping("/{disciplineId}")
    public DisciplineResponseDto getDisciplineById(@PathVariable Long disciplineId) {
        return disciplineService.getDisciplineById(disciplineId);
    }

    @Operation(
            summary = "Обновить дисциплину",
            description = "Доступно преподавателям дисциплины",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "disciplineId", description = "ID дисциплины", example = "5")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Обновленные данные дисциплины"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
    })
    @PutMapping("/{disciplineId}")
    public DisciplineResponseDto updateDiscipline(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long disciplineId,
            @Valid @RequestBody DisciplineRequestDto dto) {
        return disciplineService.updateDiscipline(userDetails.getId(), disciplineId, dto);
    }

    @Operation(
            summary = "Удалить дисциплину",
            description = "Доступно преподавателям дисциплины",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "disciplineId", description = "ID дисциплины", example = "5")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Дисциплина удалена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
    })
    @DeleteMapping("/{disciplineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiscipline(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long disciplineId) {
        disciplineService.deleteDiscipline(userDetails.getId(), disciplineId);
    }

    @Operation(
            summary = "Добавить группу к дисциплине",
            description = "Доступно преподавателям дисциплины",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameters({
            @Parameter(name = "disciplineId", description = "ID дисциплины", example = "5"),
            @Parameter(name = "groupId", description = "ID группы", example = "10")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Группа успешно добавлена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Дисциплина или группа не найдены"),
            @ApiResponse(responseCode = "409", description = "Группа уже привязана к дисциплине")
    })
    @PostMapping("/{disciplineId}/groups/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    public void addGroupToDiscipline(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long disciplineId,
            @PathVariable Long groupId) {
        disciplineService.addGroupToDiscipline(userDetails.getId(), disciplineId, groupId);
    }

    @Operation(
            summary = "Удалить группу из дисциплины",
            description = "Доступно преподавателям дисциплины",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameters({
            @Parameter(name = "disciplineId", description = "ID дисциплины", example = "5"),
            @Parameter(name = "groupId", description = "ID группы", example = "10")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Группа успешно удалена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Дисциплина или группа не найдены"),
            @ApiResponse(responseCode = "409", description = "Группа не привязана к дисциплине")
    })
    @DeleteMapping("/{disciplineId}/groups/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGroupFromDiscipline(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long disciplineId,
            @PathVariable Long groupId) {
        disciplineService.removeGroupFromDiscipline(userDetails.getId(), disciplineId, groupId);
    }

    @Operation(
            summary = "Получить группы дисциплины",
            description = "Доступно преподавателям дисциплины"
    )
    @Parameter(name = "disciplineId", description = "ID дисциплины", example = "5")
    @ApiResponse(responseCode = "200", description = "Список привязанных групп")
    @GetMapping("/{disciplineId}/groups")
    public List<GroupResponseDto> getDisciplineGroups(@PathVariable Long disciplineId) {
        return disciplineService.getDisciplineGroups(disciplineId);
    }

    @Operation(
            summary = "Получить доступные дисциплины",
            description = "Для аутентифицированных пользователей (преподаватели и студенты)",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список доступных дисциплин"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/my")
    public List<DisciplineResponseDto> getAvailableDisciplines(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        return disciplineService.getAvailableDisciplines(userDetails.getId());
    }
}
