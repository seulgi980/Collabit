package com.collabit.survey.domain.dto;

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
public class SurveyEssayResponseDTO {
    private List<SurveyEssayMessageDTO> messages;
    private LocalDateTime submittedAt;
}
