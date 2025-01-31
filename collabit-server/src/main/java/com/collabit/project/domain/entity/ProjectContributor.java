package com.collabit.project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_contributor")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectContributor{

    @EmbeddedId
    private ProjectContributorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_code", insertable = false, updatable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_info_code", insertable = false, updatable = false)
    private ProjectInfo projectInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "github_id", insertable = false, updatable = false)
    private Contributor contributor;

}