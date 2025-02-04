package com.collabit.survey.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class SurveyMultipleResponseDTO {
    @Size(min = 24, max = 24, message = "점수 리스트(scores)는 정확히 24개의 항목이 필요합니다.")
    private List<Integer> scores;
    private LocalDateTime submittedAt;
}
