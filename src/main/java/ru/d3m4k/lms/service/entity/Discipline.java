package ru.d3m4k.lms.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "disciplines")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "discipline")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "discipline")
    private List<Consultation> consultations = new ArrayList<>();

    @ManyToMany(mappedBy = "disciplines", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> teachers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "discipline_group",
            joinColumns = @JoinColumn(name = "discipline_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups = new HashSet<>();
}
