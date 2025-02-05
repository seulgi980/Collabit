package com.collabit.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetMainProjectListResponseDTO {
    private final String organization;
    private final int code;
    private final String title;
    private final int participant;
    private final boolean isDone;
    private final boolean newSurveyResponse;
    private final LocalDateTime createdAt;
    private final List<ContributorDetailDTO> contributors;
    private double participationRate;
}
