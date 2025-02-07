package com.collabit.portfolio.domain.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class MultipleAverageByUserResponseDTO {
    private List<MultipleAverageDTO> scores;
    private HighestLowestSkillDTO highestLowestSkills;
}
