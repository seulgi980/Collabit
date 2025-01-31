package com.collabit.project.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

// ProjectContributorId.java (복합키 클래스)
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectContributorId {
    @Column(name = "project_code")  // 추가
    private int projectCode;

    @Column(name = "project_info_code")  // 추가
    private int projectInfoCode;

    @Column(name = "contributor_code")  // 추가
    private int contributorCode;
}
