package ru.d3m4k.lms.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.TaskSubmission;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Long> {
    List<TaskSubmission> findByTaskId(Long taskId);
    List<TaskSubmission> findByStudentId(Long studentId);
    Optional<TaskSubmission> findByTaskIdAndStudentId(Long taskId, Long studentId);
}
