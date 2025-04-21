package ru.d3m4k.lms.service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "task_submissions")
public class TaskSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private String text;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "submission")
    private List<Grade> grades = new ArrayList<>();

    // Getters, setters, constructors
}
