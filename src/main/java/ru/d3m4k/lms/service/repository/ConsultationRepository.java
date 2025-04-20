package ru.d3m4k.lms.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.Consultation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends CrudRepository<Consultation, Long> {
    List<Consultation> findByTeacherId(Long teacherId);
}
