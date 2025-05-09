package ru.d3m4k.lms.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.d3m4k.lms.service.dto.CreateMaterialRequest;
import ru.d3m4k.lms.service.dto.MaterialResponse;
import ru.d3m4k.lms.service.dto.UpdateMaterialRequest;
import ru.d3m4k.lms.service.entity.File;
import ru.d3m4k.lms.service.entity.Material;
import ru.d3m4k.lms.service.entity.User;
import ru.d3m4k.lms.service.exception.ResourceNotFoundException;
import ru.d3m4k.lms.service.service.FileService;
import ru.d3m4k.lms.service.service.MaterialService;
import ru.d3m4k.lms.service.service.UserService;
import ru.d3m4k.lms.service.util.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Tag(name = "Material Management", description = "Управление учебными материалами")
public class MaterialController {

    private final MaterialService materialService;
    private final FileService fileService;
    private final UserService userService;

    @Operation(
            summary = "Создать новый учебный материал",
            description = "Доступно только администраторам",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Материал успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Файл не найден (если прикреплен)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialResponse createMaterial(
            @RequestBody @Valid CreateMaterialRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {

        User teacher = userService.findByUsername(userDetails.getUsername()).get();
        File file = request.getFileId() != null ? fileService.getFile(request.getFileId()) : null;

        Material material = new Material();
        material.setTitle(request.getTitle());
        material.setDescription(request.getDescription());
        material.setFile(file);
        material.setTeacher(teacher);
        material.setCreatedAt(LocalDateTime.now());

        return materialService.createMaterial(material);
    }

    @Operation(
            summary = "Получить список всех материалов",
            description = "Доступно всем авторизованным пользователям",
            security = @SecurityRequirement(name = "JWT")
    )
    @GetMapping
    public ResponseEntity<List<MaterialResponse>> getAllMaterials() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @Operation(
            summary = "Получить материал по ID",
            description = "Доступно всем авторизованным пользователям",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "id", description = "ID материала", example = "1")
    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @Operation(
            summary = "Обновить материал",
            description = "Доступно только администраторам",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "id", description = "ID материала", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Материал успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Материал не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponse> updateMaterial(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMaterialRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(materialService.updateMaterial(id, request));
    }

    @Operation(
            summary = "Удалить материал",
            description = "Доступно только администраторам",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "id", description = "ID материала", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Материал успешно удален"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Материал не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMaterial(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        materialService.deleteMaterial(id);
    }

    @Operation(
            summary = "Получить файл материала",
            description = "Доступно всем авторизованным пользователям",
            security = @SecurityRequirement(name = "JWT")
    )
    @Parameter(name = "materialId", description = "ID материала", example = "1")
    @GetMapping("/{materialId}/file")
    public ResponseEntity<Resource> downloadMaterialFile(@PathVariable Long materialId) {
        Material material = materialService.getMaterialEntityById(materialId);
        if (material.getFile() == null) {
            throw new ResourceNotFoundException("Material doesn't have attached file");
        }
        return fileService.downloadFile(material.getFile());
    }
}
