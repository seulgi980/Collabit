package com.collabit.portfolio.domain.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class MultipleTimelineResponseDTO {
    private List<TimelineDataDTO> timeline;

    private int minScore;
    private int maxScore;
}
