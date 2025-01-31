package com.collabit.project.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Contributor {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int code;

    @Column(name = "github_id", nullable = false)
    private String githubId;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @OneToMany(mappedBy = "contributor")
    private List<ProjectContributor> projectContributors;

    @Builder
    public Contributor(String githubId, String profileImage) {
        this.githubId = githubId;
        this.profileImage = profileImage;
    }
}
