package com.collabit.survey.service;

import com.collabit.global.security.SecurityUtil;
import com.collabit.project.domain.entity.ProjectContributor;
import com.collabit.project.domain.entity.ProjectInfo;
import com.collabit.project.repository.ProjectContributorRepository;
import com.collabit.project.repository.ProjectInfoRepository;
import com.collabit.survey.domain.dto.*;
import com.collabit.survey.domain.entity.SurveyEssay;
import com.collabit.survey.domain.entity.SurveyQuestion;
import com.collabit.survey.domain.entity.SurveyMultiple;
import com.collabit.survey.exception.SurveyMessageDecodingException;
import com.collabit.survey.exception.SurveyNotFInishedException;
import com.collabit.survey.repository.SurveyEssayRepository;
import com.collabit.survey.repository.SurveyMultipleRepository;
import com.collabit.survey.repository.SurveyQuestionRepository;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyEssayRepository surveyEssayRepository;
    private final SurveyMultipleRepository surveyMultipleRepository;
    private final ProjectInfoRepository projectInfoRepository;
    private final ProjectContributorRepository projectContributorRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // 유저의 설문 리스트 가져오기
    public List<SurveyListResponseDTO> getSurveyList(String userCode) {
        User user = userRepository.findByCode(userCode).orElseThrow(() -> {
            log.debug("User not found");
            return new UserNotFoundException();
        });
        log.debug("User GithubID: " + user.getGithubId());

        //나에게 들어온 모든 설문 요청 가져오기
        List<ProjectContributor> surveyRequest = projectContributorRepository.findByIdGithubId(user.getGithubId());
        // 해당 설문들의 정보 가져오기
        List<SurveyListResponseDTO> surveyList = new ArrayList<>();

        for (ProjectContributor projectContributor : surveyRequest) {
            ProjectInfo projectInfo = projectContributor.getProjectInfo();
            if (projectInfo != null) {
                int status = 0;
                LocalDateTime updatedAt = projectInfo.getCreatedAt();

                //주관식
                SurveyEssay surveyEssays = surveyEssayRepository.findByProjectInfoCodeAndUserCode(projectInfo.getCode(), userCode);
                //객관식
                SurveyMultiple surveyMultiples = surveyMultipleRepository.findByProjectInfoCodeAndUserCode(
                        projectInfo.getCode(), userCode);

                //둘 다 참여한 경우
                if (surveyEssays != null) {
                    status = 2;
                    updatedAt = surveyEssays.getSubmittedAt();
                }
                // 객관식만 참여한 경우
                else if (surveyMultiples != null) {
                    status = 1;
                    updatedAt = surveyMultiples.getSubmittedAt();
                }
                log.debug("status: " + status + ", updatedAt: " + updatedAt);

                SurveyListResponseDTO dto = SurveyListResponseDTO.builder()
                        .surveyCode(projectInfo.getCode())
                        .title(projectInfo.getProject().getTitle())
                        .profileImage(projectInfo.getUser().getProfileImage())
                        .nickname(projectInfo.getUser().getNickname())
                        .status(status)
                        .updatedAt(updatedAt)
                        .build();
                surveyList.add(dto);

                log.debug(dto.toString());
            }
        }
        return surveyList;
    }

    // 객관식 24개 모든 질문 가져오기
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
    public void saveResponse(String userCode, int projectInfoCode, List<Integer> scores) {
        SurveyMultiple surveyMultiple = SurveyMultiple.builder()
                .projectInfoCode(projectInfoCode)
                .userCode(userCode)
                .scores(scores)
                .submittedAt(LocalDateTime.now())
                .build();
        log.debug("surveyResponse: {}", surveyMultiple);

        // projectInfoCode 로 6개영역 총합값 업데이트, participant += 1
        int sympathy = scores.subList(0, 4).stream().mapToInt(Integer::intValue).sum();
        int listening = scores.subList(4, 8).stream().mapToInt(Integer::intValue).sum();
        int expression = scores.subList(8, 12).stream().mapToInt(Integer::intValue).sum();
        int problemSolving = scores.subList(12, 16).stream().mapToInt(Integer::intValue).sum();
        int conflictResolution = scores.subList(16, 20).stream().mapToInt(Integer::intValue).sum();
        int leadership = scores.subList(20, 24).stream().mapToInt(Integer::intValue).sum();

        log.debug("Calculated Scores - Sympathy: {}, Listening: {}, Expression: {}, ProblemSolving: {}, ConflictResolution: {}, Leadership: {}",
                sympathy, listening, expression, problemSolving, conflictResolution, leadership);

        projectInfoRepository.updateSurveyScores(projectInfoCode, sympathy, listening, expression, problemSolving, conflictResolution, leadership);
        surveyMultipleRepository.save(surveyMultiple);
    }

    // 객관식 설문 답변 조회하기
    public SurveyMultipleResponseDTO getMultipleResponse(String userCode, int projectInfoCode) {
        SurveyMultiple multiple = surveyMultipleRepository.findByProjectInfoCodeAndUserCode(projectInfoCode, userCode);
        if (multiple == null) throw new SurveyNotFInishedException();
        return SurveyMultipleResponseDTO.builder().scores(multiple.getScores()).submittedAt(multiple.getSubmittedAt()).build();
    }

    //주관식 설문 답변 조회하기
    public SurveyEssayResponseDTO getEssayResponse(String userCode, int projectInfoCode) {
        SurveyEssay essay = surveyEssayRepository.findByProjectInfoCodeAndUserCode(projectInfoCode, userCode);
        if (essay == null) throw new SurveyNotFInishedException();
        //메시지 string -> MessageDTO로 변환
        List<SurveyEssayMessageDTO> messageList;
        try {
            messageList = objectMapper.readValue(essay.getMessages(), new TypeReference<List<SurveyEssayMessageDTO>>() {});
        } catch (Exception e) {
            throw new SurveyMessageDecodingException();
        }

        return SurveyEssayResponseDTO.builder()
                .messages(messageList)
                .submittedAt(essay.getSubmittedAt())
                .build();
    }
}
