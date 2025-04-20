package ru.d3m4k.lms.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.Grade;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long> {
    List<Grade> findBySubmissionId(Long submissionId);
    List<Grade> findByTeacherId(Long teacherId);
    Optional<Grade> findTopBySubmissionIdOrderByGradedAtDesc(Long submissionId);
}
