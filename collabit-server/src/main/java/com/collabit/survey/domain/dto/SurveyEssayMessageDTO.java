package com.collabit.survey.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class SurveyEssayMessageDTO {
    private String role;
    private String content;
    private String timestamp;
}
