package com.collabit.survey.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

// 객관식 설문 전체 답변 Entity
@Document(collection = "responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {
    @Id
    private String id;
    private int projectInfoCode; // 설문지 정보(설문 요청자+프로젝트코드 등..)
    private String userCode; // 설문 답변자
    private List<Integer> scores; // 총 24개 점수만 저장. 문항은 index 로 판별
    private LocalDateTime submittedAt;
}
