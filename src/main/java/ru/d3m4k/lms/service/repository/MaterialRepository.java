package ru.d3m4k.lms.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.Material;

import java.util.List;

@Repository
public interface MaterialRepository extends CrudRepository<Material, Long> {
    List<Material> findByTeacherId(Long teacherId);
    List<Material> findByTitleContainingIgnoreCase(String title);
}
