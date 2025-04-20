package ru.d3m4k.lms.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.ConsultationGroup;

import java.util.List;

@Repository
public interface ConsultationGroupRepository extends CrudRepository<ConsultationGroup, Long> {
    List<ConsultationGroup> findByConsultationId(Long consultationId);
    List<ConsultationGroup> findByGroupId(Long groupId);
    boolean existsByConsultationIdAndGroupId(Long consultationId, Long groupId);
}
