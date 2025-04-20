package ru.d3m4k.lms.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    List<File> findByUploadedById(Long userId);
    Optional<File> findByFilePath(String filePath);
}
