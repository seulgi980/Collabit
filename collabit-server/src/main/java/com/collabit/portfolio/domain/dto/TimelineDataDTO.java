package com.collabit.portfolio.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class TimelineDataDTO {
    private String projectName;
    private String organization;
    private LocalDateTime completedAt;
    private Map<String, ScoreDTO> scores;
}
