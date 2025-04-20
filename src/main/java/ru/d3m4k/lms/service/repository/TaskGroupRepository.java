package ru.d3m4k.lms.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.TaskGroup;

import java.util.List;

@Repository
public interface TaskGroupRepository extends CrudRepository<TaskGroup, Long> {
    List<TaskGroup> findByTaskId(Long taskId);
    List<TaskGroup> findByGroupId(Long groupId);
    boolean existsByTaskIdAndGroupId(Long taskId, Long groupId);
}
