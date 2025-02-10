package com.collabit.project.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TotalScore {
    @Id
    @Column(name = "total_participant")
    private Integer totalParticipant;  // 이미 있는 필드를 PK로 사용

    @Column(name = "sympathy")
    private Long sympathy;

    @Column(name = "listening")
    private Long listening;

    @Column(name = "expression")
    private Long expression;

    @Column(name = "problem_solving")
    private Long problemSolving;

    @Column(name = "conflict_resolution")
    private Long conflictResolution;

    @Column(name = "leadership")
    private Long leadership;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
