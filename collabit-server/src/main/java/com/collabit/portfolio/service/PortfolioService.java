package com.collabit.portfolio.service;

import com.collabit.global.common.ErrorCode;
import com.collabit.global.error.exception.BusinessException;
import com.collabit.portfolio.domain.dto.*;
import com.collabit.portfolio.repository.DescriptionRepository;
import com.collabit.portfolio.repository.FeedbackRepository;
import com.collabit.portfolio.repository.projection.CodeNameProjection;
import com.collabit.portfolio.repository.projection.DescriptionProjection;
import com.collabit.portfolio.repository.projection.FeedbackProjection;
import com.collabit.project.domain.entity.Project;
import com.collabit.project.domain.entity.ProjectInfo;
import com.collabit.project.repository.ProjectInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final ProjectInfoRepository projectInfoRepository;
    private final DescriptionRepository descriptionRepository;
    private final FeedbackRepository feedbackRepository;

    // hexagon + progress bar 그래프 데이터 채우기
    public MultipleHexagonProgressResponseDTO getPortfolioHexagonAndProgressbarGraph(String userCode) {

        // =========== HEXAGON ==========
        // 전체 "마감된" 프로젝트 평균 계산 <code : 평균값>
        Map<String, Double> userAverages = getUserAverage(userCode);
        log.debug("userAverages: {}", userAverages);

        // 사이트 전체 유저 "마감된" 프로젝트 평균 점수 조회 <code : 평균값>
        Map<String, Double> totalUserAverages = getTotalUserAverage();
        log.debug("totalUserAverages: {}", totalUserAverages);

        // 해당되는 Feedback 데이터 조회 <code : FeedbackProjection>
        Map<String, FeedbackProjection> feedbackData = getFeedback(userAverages, totalUserAverages);
        log.debug("feedbackData: {}", feedbackData);

        // Description 데이터 조회 <code : description>
        Map<String, String> descriptionData = getDescriptions();
        log.debug("descriptionData: {}", descriptionData);

        // HexagonDataDTO 채우기
        Map<String, HexagonDataDTO> hexagonDataMap = userAverages.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            String code = entry.getKey();
                            double userScore = entry.getValue();
                            double totalScore = totalUserAverages.getOrDefault(code, 0.0);

                            FeedbackProjection feedbackProjection = feedbackData.get(code);
                            boolean isPositive = feedbackProjection.getIsPositive();
                            String feedback = feedbackProjection.getFeedback();
                            String name = feedbackProjection.getName();

                            String description = descriptionData.get(code);
                            return HexagonDataDTO.builder()
                                    .name(name)
                                    .score(userScore)
                                    .total(totalScore)
                                    .feedback(feedback)
                                    .description(description)
                                    .isPositive(isPositive)
                                    .build();
                        }
                        ));
        log.debug("hexagonDataMap: {}", hexagonDataMap);

        HexagonDTO hexagonDTO =  new HexagonDTO(hexagonDataMap, 1, 5);

        // =========== ProgressBar ==========
        Map<String, ProgressDataDTO> progressDataMap = userAverages.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            String code = entry.getKey();
                            double userAverage = entry.getValue();
                            double totalUserAverage = totalUserAverages.getOrDefault(code, 0.0);

                            // Progress Bar 값 계산
                            int progressBarValue = calculateProgressBarValue(userAverage, totalUserAverage, 1.0, 5.0);
                            String name = feedbackData.get(code).getName();

                            return ProgressDataDTO.builder()
                                    .name(name)
                                    .score(progressBarValue)
                                    .build();
                        }
                ));


        ProgressDTO progressDTO = new ProgressDTO(progressDataMap, 1, 5);


        return new MultipleHexagonProgressResponseDTO(hexagonDTO, progressDTO);
    }

    // 유저별 평균 계산
    private Map<String, Double>  getUserAverage(String userCode) {
        // 유저가 참여한 모든 프로젝트 조회(단 설문이 종료된 것만)
        List<ProjectInfo> projectInfos = projectInfoRepository.findAllCompletedByUserCode(userCode);
        log.debug("projectInfos: {}", projectInfos);

        if (projectInfos.isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_INFO_NOT_FOUND);
        }

        return calculateProjectInfosAverageScores(projectInfos);
    }

    // 전체 사용자 평균 계산
    private Map<String, Double> getTotalUserAverage() {
        List<ProjectInfo> projectInfos = projectInfoRepository.findAllCompleted();
        log.debug("projectInfos: {}", projectInfos);

        if (projectInfos.isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_INFO_NOT_FOUND);
        }

        return calculateProjectInfosAverageScores(projectInfos);
    }

    // 여러개의 projectinfo 들의 평균 계산 메서드
    private Map<String, Double> calculateProjectInfosAverageScores(List<ProjectInfo> projectInfos) {
        // 점수합, 참여자합 누적할 변수
        Map<String, Integer> totalScores = new HashMap<>();
        int totalParticipant = 0;

        // 각 프로젝트마다의 점수 합산
        for (ProjectInfo projectInfo : projectInfos) {

            totalScores.merge("sympathy", projectInfo.getSympathy(), Integer::sum);
            totalScores.merge("listening", projectInfo.getListening(), Integer::sum);
            totalScores.merge("expression", projectInfo.getExpression(), Integer::sum);
            totalScores.merge("problem_solving", projectInfo.getProblemSolving(), Integer::sum);
            totalScores.merge("conflict_resolution", projectInfo.getConflictResolution(), Integer::sum);
            totalScores.merge("leadership", projectInfo.getLeadership(), Integer::sum);

            totalParticipant += projectInfo.getParticipant();
        }

        // 총점 데이터와 참여자 수로 5점 만점의 평균 계산
        Map<String, Double> averageScores = new HashMap<>();

        if (totalParticipant > 0) {
            for (Map.Entry<String, Integer> entry : totalScores.entrySet()) {
                String code = entry.getKey();
                int totalScore = entry.getValue();
                double average = (double) totalScore / totalParticipant;
                average = Math.round(average * 10.0) / 10.0; // 소수점 1자리 반올림
                averageScores.put(code, average);
            }
        } else {
            // 참여자가 0명인 경우 0.0으로 설정
            totalScores.keySet()
                    .forEach(key -> averageScores.put(key, 0.0));
        }

        log.debug("averageScores: {}", averageScores);

        return averageScores;
    }


    // feedback 정보 가져오기
    private Map<String, FeedbackProjection> getFeedback(Map<String, Double> userAverage, Map<String, Double> totalUserAverage) {

        return userAverage.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            String code = entry.getKey();
                            double totalAverageScore = totalUserAverage.getOrDefault(code, 0.0);
                            boolean isPositive = entry.getValue() >= totalAverageScore;

                            FeedbackProjection feedback = feedbackRepository.findByCodeAndIsPositive(code, isPositive);

                            if (feedback == null) {
                                throw new BusinessException(ErrorCode.FEEDBACK_NOT_FOUND);
                            }

                            return feedback;
                        }
                ));
    }

    private Map<String, String> getDescriptions() {
        return descriptionRepository.findAllProjectedBy().stream()
                .collect(Collectors.toMap(DescriptionProjection::getCode, DescriptionProjection::getDescription));
    }

    // 유저별 평균점수 상대위치 계산
    private int calculateProgressBarValue(double userAverage, double totalAverageScore, double minBaseScore, double maxBaseScore) {
        if (userAverage >= totalAverageScore) {
            // 평균 이상인 경우 (50~100)
            double fraction = (userAverage - totalAverageScore) / (maxBaseScore - totalAverageScore);
            return (int) Math.round(fraction * 50) + 50;
        } else {
            // 평균 이하인 경우 (0~50)
            double fraction = (userAverage - minBaseScore) / (totalAverageScore - minBaseScore);
            return (int) Math.round(fraction * 50);
        }
    }

    // timeline 그래프 데이터 채우기
    public MultipleTimelineResponseDTO getPortfolioTimelineGraph(String userCode) {
        // 완료된 프로젝트중 최근 8개까지 조회
        List<ProjectInfo> projectInfos = projectInfoRepository.findTop8ByUserCodeAndCompletedAtIsNotNullOrderByCompletedAtDesc(userCode);

        if(projectInfos.isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_INFO_NOT_FOUND);
        }

        Map<String, String> names = getDistinctFeedbackNames();

        List<TimelineDataDTO> timelinedDataDTOs = projectInfos.stream()
                .map(projectInfo -> {
                    int participant = projectInfo.getParticipant();

                    Map<String, Integer> totalScores = Map.of(
                            "sympathy", projectInfo.getSympathy(),
                            "listening", projectInfo.getListening(),
                            "expression", projectInfo.getExpression(),
                            "problem_solving", projectInfo.getProblemSolving(),
                            "conflict_resolution", projectInfo.getConflictResolution(),
                            "leadership", projectInfo.getLeadership()
                    );

                    Map<String, ScoreDTO> scores = totalScores.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> ScoreDTO.builder()
                                            .name(names.get(entry.getKey())) // code로 name찾기
                                            .score(participant > 0 ? Math.round((double) entry.getValue() / participant * 10.0) / 10.0 : 0.0)
                                            .build()
                            ));

                    Project project = projectInfo.getProject();

                    return TimelineDataDTO.builder()
                            .projectName(project.getTitle())
                            .organization(project.getOrganization())
                            .completedAt(projectInfo.getCompletedAt())
                            .scores(scores)
                            .build();

                })
                .collect(Collectors.toList());


        return new MultipleTimelineResponseDTO(timelinedDataDTOs, 1, 5);
    }

    // name 가져오기
    public Map<String, String> getDistinctFeedbackNames() {
        return feedbackRepository.findDistinctCodeAndNameBy()
                .stream()
                .collect(Collectors.toMap(CodeNameProjection::getCode, CodeNameProjection::getName));
    }




}


