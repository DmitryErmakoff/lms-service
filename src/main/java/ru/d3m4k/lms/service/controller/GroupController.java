package ru.d3m4k.lms.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.d3m4k.lms.service.dto.*;
import ru.d3m4k.lms.service.exception.ResourceConflictException;
import ru.d3m4k.lms.service.exception.ResourceNotFoundException;
import ru.d3m4k.lms.service.service.GroupService;
import ru.d3m4k.lms.service.util.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponseDto createGroup(@Valid @RequestBody GroupRequestDto dto) {
        return groupService.createGroup(dto);
    }

    @GetMapping("/{groupId}/students")
    public List<UserDto> getGroupStudents(@PathVariable Long groupId) {
        return groupService.getGroupStudents(groupId);
    }

    @GetMapping("/group")
    public StudentsFromGroupResponseDto getStudentsFromGroup(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return groupService.getStudentsFromGroup(userDetails.getUsername());
    }

    @GetMapping
    public List<GroupResponseDto> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/users/unassigned")
    public List<UserDto> getUsersWithoutGroup() {
        return groupService.getUsersWithoutGroup();
    }

    @PutMapping("/{groupId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupService.addUserToGroup(groupId, userId);
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.removeUserFromGroup(groupId, userId);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(ResourceConflictException ex) {
        return new ErrorResponse("CONFLICT", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ErrorResponse("VALIDATION_ERROR", message, LocalDateTime.now());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse("BAD_REQUEST", ex.getMessage(), LocalDateTime.now());
    }
}
