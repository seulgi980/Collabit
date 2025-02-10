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
    private Map<String, ProgressDataDTO> progressData;
    private int minScore;
    private int maxScore;
}


