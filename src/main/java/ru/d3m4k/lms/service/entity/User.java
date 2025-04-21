package ru.d3m4k.lms.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String surname;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(unique = true, nullable = false, length = 50)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @ManyToMany
    @JoinTable(
            name = "teacher_disciplines",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    @ToString.Exclude  // Важно!
    @EqualsAndHashCode.Exclude  // Исключаем из hashCode/equals
    private Set<Discipline> disciplines = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private List<Material> materials = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "uploadedBy")
    private List<File> files = new ArrayList<>();
}


