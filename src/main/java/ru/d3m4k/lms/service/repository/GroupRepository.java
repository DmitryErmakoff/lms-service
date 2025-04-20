package ru.d3m4k.lms.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.Group;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);
    Boolean existsByName(String name);
    List<Group> findByCreatedAtAfter(LocalDateTime date);
    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.users WHERE g.id = :id")
    Optional<Group> findByIdWithUsers(@Param("id") Long id);
}
