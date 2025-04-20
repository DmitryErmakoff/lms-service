package ru.d3m4k.lms.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "disciplines")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany(mappedBy = "disciplines")
    private Set<User> teachers;
}
