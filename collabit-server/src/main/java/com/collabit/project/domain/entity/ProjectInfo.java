package com.collabit.project.domain.entity;

import com.collabit.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_code")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private int participant = 0;

    @Column(name = "is_done", nullable = false)
    private boolean isDone = false;

    @Column(nullable = false)
    private int sympathy = 0;

    @Column(nullable = false)
    private int listening = 0;

    @Column(nullable = false)
    private int expression = 0;

    @Column(name = "problem_solving", nullable = false)
    private int problemSolving = 0;

    @Column(name = "conflict_resolution", nullable = false)
    private int conflictResolution = 0;

    @Column(nullable = false)
    private int leadership = 0;

    @OneToMany(mappedBy = "projectInfo")
    private List<ProjectContributor> projectContributors;
}
