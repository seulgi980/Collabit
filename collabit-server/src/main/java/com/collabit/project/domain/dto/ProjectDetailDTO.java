package com.collabit.project.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ProjectDetailDTO {
    private final int code;
    private final String title;
    private final int participant;
    private final boolean isDone;
    private final boolean newSurveyResponse;
    private final LocalDateTime createdAt;
    private final List<ContributorDetailDTO> contributors;
    private double participationRate;
}
