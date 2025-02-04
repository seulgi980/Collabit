package com.collabit.survey.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.survey.domain.dto.ApiTextResponseDTO;
import com.collabit.survey.domain.dto.SurveyQuestionResponseDTO;
import com.collabit.survey.domain.dto.SurveyResponseSaveRequestDTO;
import com.collabit.survey.domain.entity.SurveyResponse;
import com.collabit.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
@Tag(name = "SurveyController", description = "객관식 설문 질문문항/답변 관련 API")
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/check")
    @Operation(summary="특정 userCode 유저가 특정 projectInfoCode 의 설문에 참여했는지 확인", description = "A가 projectInfoCode 1번의 설문을 이미 완료했는지 확인하는 API 입니다.(true면 참여완료)")
    public ResponseEntity<Boolean> checkIfUserCanEvaluate(
            @RequestParam int projectInfoCode, HttpServletRequest request) {
        // userCode 추출
        String userCode = SecurityUtil.getCurrentUserCode();

        boolean canEvaluate = surveyService.canUserEvaluate(projectInfoCode, request, userCode);
        return ResponseEntity.ok(!canEvaluate);
    }

    @GetMapping("/questions")
    @Operation(summary="객관식 질문 24개 조회", description = "객관식 설문 질문 문항 전체 24개를 조회하는 API 입니다.")
    public ResponseEntity<List<SurveyQuestionResponseDTO>> findAllQuestions() {
        return ResponseEntity.ok(surveyService.getAllQuestions());
    }

    @PostMapping("/response/save/{projectInfoCode}")
    @Operation(summary="객관식 설문 답변 저장하기", description = "24개의 객관식 설문 답변내용을 mongoDB에 저장하는 API 입니다.")
    public ResponseEntity<ApiTextResponseDTO> saveResponse(
            @PathVariable int projectInfoCode,
            @RequestBody @Valid SurveyResponseSaveRequestDTO surveyResponseSaveRequestDTO,
            HttpServletRequest request) {

        List<Integer> scores = surveyResponseSaveRequestDTO.getScores();

        // userCode 추출
        String userCode = SecurityUtil.getCurrentUserCode();

        SurveyResponse surveyResponse = surveyService.saveResponse(projectInfoCode, scores, request, userCode);
        log.debug("surveyResponse: {}", surveyResponse);
        return ResponseEntity.ok(new ApiTextResponseDTO("설문 응답이 성공적으로 저장되었습니다."));
    }

}
