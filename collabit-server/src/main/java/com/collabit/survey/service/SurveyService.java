package com.collabit.survey.service;

import com.collabit.global.security.TokenProvider;
import com.collabit.survey.domain.dto.SurveyQuestionResponseDTO;
import com.collabit.survey.domain.entity.SurveyQuestion;
import com.collabit.survey.domain.entity.SurveyResponse;
import com.collabit.survey.repository.SurveyProjectInfoRepository;
import com.collabit.survey.repository.SurveyQuestionRepository;
import com.collabit.survey.repository.SurveyResponseRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final TokenProvider tokenProvider;
    private final SurveyProjectInfoRepository surveyProjectInfoRepository;

    // 특정 userCode 유저 -> 특정 projectInfoCode 설문 참여 가능 여부 확인
    public boolean canUserEvaluate(int projectInfoCode, HttpServletRequest request) {
        // request 의 cookie 에서 access token 추출
        String accessToken = extractToken(request, "accessToken");

        // token 에서 userCode 추출
        String userCode = tokenProvider.parseClaims(accessToken).getSubject();

        // userCode 유저가 projectInfoCode 설문에 참여했는지 확인
        List<SurveyResponse> existingResponses = surveyResponseRepository.findByProjectInfoCodeAndUserCode(
                projectInfoCode, userCode);
        return existingResponses.isEmpty(); // 평가 내역이 없으면 설문 가능
    }

    // 24개 모든 질문 가져오기
    public List<SurveyQuestionResponseDTO> getAllQuestions() {
        List<SurveyQuestion> questions = surveyQuestionRepository.findAll();

        if (questions.size() != 24) {
            throw new IllegalStateException("설문 문항이 24개가 아닙니다.");
        }

        List<SurveyQuestionResponseDTO> result = new ArrayList<>();
        for(int i=0; i<24; i++) {
            SurveyQuestion surveyQuestion = questions.get(i);
            result.add(new SurveyQuestionResponseDTO(surveyQuestion.getQuestionNumber(), surveyQuestion.getQuestionText()));
        }
        return result;
    }

    // 객관식 설문 결과 저장하기
    public SurveyResponse saveResponse(int projectInfoCode, List<Integer> scores, HttpServletRequest request) {
        // request 의 cookie 에서 access token 추출
        String accessToken = extractToken(request, "accessToken");
        log.debug("accessToken: {}", accessToken);
        // token 에서 userCode 추출

        String userCode = tokenProvider.parseClaims(accessToken).getSubject();
        log.debug("userCode: {}", userCode);

        SurveyResponse surveyResponse = SurveyResponse.builder()
                .projectInfoCode(projectInfoCode)
                .userCode(userCode)
                .scores(scores)
                .submittedAt(LocalDateTime.now())
                .build();
        log.debug("surveyResponse: {}", surveyResponse);

        // projectInfoCode 로 6개영역 총합값 업데이트, participant += 1
        int sympathy = scores.subList(0, 4).stream().mapToInt(Integer::intValue).sum();
        int listening = scores.subList(4, 8).stream().mapToInt(Integer::intValue).sum();
        int expression = scores.subList(8, 12).stream().mapToInt(Integer::intValue).sum();
        int problemSolving = scores.subList(12, 16).stream().mapToInt(Integer::intValue).sum();
        int conflictResolution = scores.subList(16, 20).stream().mapToInt(Integer::intValue).sum();
        int leadership = scores.subList(20, 24).stream().mapToInt(Integer::intValue).sum();

        log.debug("Calculated Scores - Sympathy: {}, Listening: {}, Expression: {}, ProblemSolving: {}, ConflictResolution: {}, Leadership: {}",
                sympathy, listening, expression, problemSolving, conflictResolution, leadership);

        // project_info 테이블 업데이트
        surveyProjectInfoRepository.updateSurveyScores(projectInfoCode, sympathy, listening, expression, problemSolving, conflictResolution, leadership);

        return surveyResponseRepository.save(surveyResponse);
    }

    // cookie에서 특정 token 추출 메서드
    private String extractToken(HttpServletRequest request, String tokenName) {
        if(request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> tokenName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}
