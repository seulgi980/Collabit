package com.collabit.survey.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SurveyQuestionResponseDTO {
    private int questionNumber;
    private String questionText;
}
