package ru.d3m4k.lms.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.Discipline;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    // Стандартный метод для поиска по названию
    Optional<Discipline> findByName(String name);

    // Метод с явной загрузкой связанных сущностей
    @EntityGraph(attributePaths = {"groups", "teachers", "consultations"})
    @Query("SELECT d FROM Discipline d WHERE d.id = :id")
    Optional<Discipline> findByIdWithRelations(@Param("id") Long id);

    // Метод для проверки существования связи с преподавателем
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +  // ← Исправлено td на t
            "FROM Discipline d " +
            "JOIN d.teachers t " +      // t - псевдоним для teachers
            "WHERE d.id = :disciplineId AND t.id = :teacherId")
    boolean existsTeacherInDiscipline(@Param("disciplineId") Long disciplineId,
                                      @Param("teacherId") Long teacherId);

    // Метод для поиска дисциплин с группами
    @EntityGraph(attributePaths = "groups")
    @Query("SELECT d FROM Discipline d WHERE d.id = :id")
    Optional<Discipline> findByIdWithGroups(@Param("id") Long id);

    // Для преподавателей
    @EntityGraph(attributePaths = {"teachers", "groups"})
    List<Discipline> findByTeachersId(Long teacherId);

    // Для студентов
    @EntityGraph(attributePaths = {"teachers", "groups"})
    List<Discipline> findByGroupsId(Long groupId);
}
