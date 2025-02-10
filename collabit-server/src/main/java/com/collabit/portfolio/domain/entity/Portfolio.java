package com.collabit.portfolio.domain.entity;

import com.collabit.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {
    @Id
    @Column(name = "user_code")
    private String userCode;

    @Column(nullable = false)
    private int project;

    @Column(nullable = false)
    private int participant;

    @Column(nullable = false)
    private Long sympathy = 0L;

    @Column(nullable = false)
    private Long listening = 0L;

    @Column(nullable = false)
    private Long expression = 0L;

    @Column(nullable = false)
    private Long problemSolving = 0L;

    @Column(nullable = false)
    private Long conflictResolution = 0L;

    @Column(nullable = false)
    private Long leadership = 0L;

    @Column
    private Boolean isUpdate = false;

    @Column
    private LocalDateTime updatedAt;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    public void changeUpdateStatus(){
        this.isUpdate = true;
    }
}
