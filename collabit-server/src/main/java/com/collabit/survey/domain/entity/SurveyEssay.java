package com.collabit.survey.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

// 주관식 설문 전체 답변 Entity
@Document(collection = "survey_essay")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyEssay {
    @Id
    private String id;
    private int projectInfoCode;
    private String userCode;
    private String messages;
    private LocalDateTime submittedAt;
}
