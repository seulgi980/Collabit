package com.collabit.survey.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// 객관식 설문 질문 Entity(Q1~Q24 24개 문항 저장)
@Document(collection = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestion {
    @Id
    private String id;
    private String category;
    private int questionNumber;
    private String questionText;
}
