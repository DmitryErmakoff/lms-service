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
import ru.d3m4k.lms.service.dto.*;
import ru.d3m4k.lms.service.service.GroupService;
import ru.d3m4k.lms.service.util.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "Groups Management", description = "Управление учебными группами")
public class GroupController {
    private final GroupService groupService;

    @Operation(
            summary = "Обновить группу",
            description = "Доступно преподавателям",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "groupId", description = "ID группы", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Группа успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена"),
            @ApiResponse(responseCode = "409", description = "Название группы уже используется")
    })
    @PutMapping("/{groupId}")
    public GroupResponseDto updateGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody GroupUpdateRequestDto dto) {
        return groupService.updateGroup(groupId, dto);
    }

    @Operation(
            summary = "Удалить группу",
            description = "Доступно преподавателям",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "groupId", description = "ID группы", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Группа успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена"),
            @ApiResponse(responseCode = "409", description = "Невозможно удалить непустую группу")
    })
    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
    }

    @Operation(
            summary = "Создать новую группу",
            description = "Доступно преподавателям",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Группа успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "409", description = "Группа с таким именем уже существует")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponseDto createGroup(@Valid @RequestBody GroupRequestDto dto) {
        return groupService.createGroup(dto);
    }

    @Operation(
            summary = "Получить студентов группы",
            description = "Доступно преподавателям"
    )
    @Parameter(name = "groupId", description = "ID группы", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список студентов группы"),
            @ApiResponse(responseCode = "404", description = "Группа не найдена")
    })
    @GetMapping("/{groupId}/students")
    public List<UserDto> getGroupStudents(@PathVariable Long groupId) {
        return groupService.getGroupStudents(groupId);
    }

    @Operation(
            summary = "Получить студентов своей группы",
            description = "Для аутентифицированных пользователей",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о группе пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не состоит в группе")
    })
    @GetMapping("/group")
    public StudentsFromGroupResponseDto getStudentsFromGroup(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        return groupService.getStudentsFromGroup(userDetails.getUsername());
    }

    @Operation(
            summary = "Получить все группы",
            description = "Доступно преподавателям"
    )
    @ApiResponse(responseCode = "200", description = "Список всех групп")
    @GetMapping
    public List<GroupResponseDto> getAllGroups() {
        return groupService.getAllGroups();
    }

    @Operation(
            summary = "Получить пользователей без группы",
            description = "Доступно преподавателям"
    )
    @ApiResponse(responseCode = "200", description = "Список непривязанных пользователей")
    @GetMapping("/users/unassigned")
    public List<UserDto> getUsersWithoutGroup() {
        return groupService.getUsersWithoutGroup();
    }

    @Operation(
            summary = "Добавить пользователя в группу",
            description = "Доступно преподавателям",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameters({
            @Parameter(name = "groupId", description = "ID группы", example = "1"),
            @Parameter(name = "userId", description = "ID пользователя", example = "5")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Обновленные данные пользователя"),
            @ApiResponse(responseCode = "404", description = "Группа или пользователь не найдены"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже состоит в группе")
    })
    @PutMapping("/{groupId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupService.addUserToGroup(groupId, userId);
    }

    @Operation(
            summary = "Удалить пользователя из группы",
            description = "Доступно преподавателям",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameters({
            @Parameter(name = "groupId", description = "ID группы", example = "1"),
            @Parameter(name = "userId", description = "ID пользователя", example = "5")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Группа или пользователь не найдены"),
            @ApiResponse(responseCode = "409", description = "Пользователь не состоит в указанной группе")
    })
    @DeleteMapping("/{groupId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.removeUserFromGroup(groupId, userId);
    }
}
