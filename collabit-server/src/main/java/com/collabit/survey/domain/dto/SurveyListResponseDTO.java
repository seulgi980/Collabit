package com.collabit.survey.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SurveyListResponseDTO {
    private int surveyCode;
    private String title;
    private String profileImage;
    private String nickname;
    private int status;
    private LocalDateTime updatedAt;
}
