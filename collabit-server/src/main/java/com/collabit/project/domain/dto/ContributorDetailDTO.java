package com.collabit.project.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContributorDetailDTO {
    private String githubId;
    private String profileImage;
}
