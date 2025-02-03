package com.collabit.survey.domain.dto;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@ToString
public class SurveyResponseSaveRequestDTO {
    @Size(min = 24, max = 24, message = "점수 리스트(scores)는 정확히 24개의 항목이 필요합니다.")
    private List<Integer> scores;
}
