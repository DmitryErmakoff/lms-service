package ru.d3m4k.lms.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.d3m4k.lms.service.dto.FileResponse;
import ru.d3m4k.lms.service.entity.File;
import ru.d3m4k.lms.service.service.FileService;
import ru.d3m4k.lms.service.util.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Management", description = "Управление файлами")
public class FileController {
    private final FileService fileService;

    @Operation(
            summary = "Загрузить файл",
            description = "Доступно только администраторам",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Файл успешно загружен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "400", description = "Ошибка при обработке файла")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        return fileService.uploadFile(file, userDetails.getId());
    }

    @Operation(
            summary = "Получить файл по ID",
            description = "Доступно студентам и преподавателям"
    )
    @Parameter(name = "id", description = "ID файла", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Файл найден"),
            @ApiResponse(responseCode = "404", description = "Файл не найден"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {

        File file = fileService.getFile(id);

        return fileService.downloadFile(file);
    }

    @Operation(
            summary = "Получить все файлы",
            description = "Доступно только администраторам",
            security = @SecurityRequirement(name = "JWT")
    )
    @GetMapping
    public ResponseEntity<List<FileResponse>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }
}
