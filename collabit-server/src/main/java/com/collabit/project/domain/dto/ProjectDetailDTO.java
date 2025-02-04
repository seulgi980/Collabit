package com.collabit.project.domain.dto;

import lombok.*;

import java.sql.Timestamp;
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
    private final LocalDateTime createdAt;
    private final List<ContributorDetailDTO> contributors;
    private double participationRate;
}
