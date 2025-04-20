package ru.d3m4k.lms.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByTeacherId(Long teacherId);
    List<Task> findByDeadlineBefore(LocalDateTime date);
    List<Task> findByMaterialId(Long materialId);
}
