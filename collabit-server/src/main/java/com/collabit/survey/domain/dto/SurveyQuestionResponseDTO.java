package com.collabit.survey.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SurveyQuestionResponseDTO {
    private int questionNumber;
    private String questionText;
}
