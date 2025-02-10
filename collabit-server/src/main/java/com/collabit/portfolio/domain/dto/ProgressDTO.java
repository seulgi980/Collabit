package com.collabit.portfolio.domain.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class ProgressDTO {
    private ProgressDataDTO sympathy;
    private ProgressDataDTO listening;
    private ProgressDataDTO expression;
    private ProgressDataDTO problemSolving;
    private ProgressDataDTO conflictResolution;
    private ProgressDataDTO leadership;

    private int minScore;
    private int maxScore;
}


