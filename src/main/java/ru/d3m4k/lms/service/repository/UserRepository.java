package ru.d3m4k.lms.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.d3m4k.lms.service.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String login);
    List<User> findByGroupId(Long groupId);
    @Query("SELECT u FROM User u WHERE u.group IS NULL")
    List<User> findUsersWithoutGroup();
    @EntityGraph(attributePaths = {"roles", "group"})
    Optional<User> findUserWithRolesAndGroupById(Long id);
}
