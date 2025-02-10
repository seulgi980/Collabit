package com.collabit.project.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class GetHexagonResponseDTO {
    int minBaseScore;
    int maxBaseScore;
    List<SkillFeedback> belowAverage;
    List<SkillFeedback> aboveAverage;
    List<SkillData> personalData;
}
