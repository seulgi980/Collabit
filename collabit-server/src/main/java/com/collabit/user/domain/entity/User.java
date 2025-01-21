package com.collabit.user.domain.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name = "code")
    private final String code = UUID.randomUUID().toString();

    @Column(name = "id")
    private String id;

    @Column(name = "github_id", unique = true)
    private String githubId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", length = 20, nullable = false)
    private String nickname;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 일반 회원가입
    @Builder
    public User(String id, String password, String nickname, String profileImage) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.githubId = null;
    }

    // GitHub 회원가입
    @Builder(builderMethodName = "githubBuilder")
    public User(String githubId, String nickname, String profileImage) {
        this.githubId = githubId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.id = null;
        this.password = null;
    }

    // 일반회원에 GitHub 연동을 위한 메서드
    public void linkGithub(String githubId) {
        this.githubId = githubId;
    }
}