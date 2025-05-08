package ru.d3m4k.lms.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.d3m4k.lms.service.dto.FileResponse;
import ru.d3m4k.lms.service.entity.File;
import ru.d3m4k.lms.service.entity.User;
import ru.d3m4k.lms.service.exception.FileStorageException;
import ru.d3m4k.lms.service.exception.ResourceNotFoundException;
import ru.d3m4k.lms.service.repository.FileRepository;
import ru.d3m4k.lms.service.repository.UserRepository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public File getFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
    }

    public List<FileResponse> getAllFiles() {
        List<File> files = fileRepository.findAll();
        return files.stream()
                .map(file -> modelMapper.map(file, FileResponse.class))
                .collect(Collectors.toList());
    }

    public FileResponse uploadFile(MultipartFile file, Long id) {
        validateFile(file);


        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        String storageName = generateStorageFileName(fileExtension);
        Path filePath = Paths.get("uploads").resolve(storageName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            File newFile = new File();
            newFile.setFileName(originalFileName);
            newFile.setFilePath(filePath.toString());
            newFile.setUploadedBy(userRepository.findById(id).get());
            newFile.setCreatedAt(LocalDateTime.now());

            File savedFile = fileRepository.save(newFile);
            log.info("File uploaded successfully: {}", savedFile.getId());

            return mapToResponse(savedFile);
        } catch (IOException ex) {
            log.error("Failed to store file", ex);
            throw new FileStorageException("Failed to store file", ex);
        }
    }

    public ResponseEntity<Resource> downloadFile(File file) {
        Path path = Paths.get(file.getFilePath());

        if (!Files.exists(path)) {
            throw new ResourceNotFoundException("File not found on server: " + file.getFileName());
        }

        try {
            Resource resource = new UrlResource(path.toUri());
            String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(determineContentType(file.getFileName()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new FileStorageException("Invalid file path", e);
        } catch (UnsupportedEncodingException e) {
            throw new FileStorageException("Encoding error", e);
        }
    }

    // Вспомогательные методы
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().contains("..")) {
            throw new IllegalArgumentException("Invalid file name");
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex == -1 ? "" : filename.substring(dotIndex);
    }

    private String generateStorageFileName(String extension) {
        return UUID.randomUUID().toString() + extension;
    }

    private MediaType determineContentType(String filename) {
        String contentType = URLConnection.guessContentTypeFromName(filename);
        return contentType != null ?
                MediaType.parseMediaType(contentType) :
                MediaType.APPLICATION_OCTET_STREAM;
    }

    private FileResponse mapToResponse(File file) {
        return new FileResponse(
                file.getId(),
                file.getFileName(),
                file.getUploadedBy().getId(),
                file.getCreatedAt()
        );
    }
}