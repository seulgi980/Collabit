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
import com.collabit.project.domain.entity.TotalScore;
import com.collabit.project.repository.ProjectInfoRepository;
import com.collabit.project.repository.TotalScoreRepository;
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
    private final TotalScoreRepository totalScoreRepository;

    public getMultipleHexagonProgressResponseDTO getHexagonAndProgressbarGraph(String userCode) {
        Map<String, Double> userAverages = getUserAverage(userCode);
        Map<String, Double> totalUserAverages = getTotalUserAverage();

        getProgressResponseDTO.builder()
            .sympathy(buildScoreData("sympathy", userAverages.get("sympathy"), totalUserAverages.get("sympathy")))
            .listening(buildScoreData("listening", userAverages.get("listening"), totalUserAverages.get("listening")))
            .expression(buildScoreData("expression", userAverages.get("expression"), totalUserAverages.get("expression")))
            .problemSolving(buildScoreData("problemSolving", userAverages.get("problem_solving"), totalUserAverages.get("problem_solving")))
            .conflictResolution(buildScoreData("conflictResolution", userAverages.get("conflict_resolution"), totalUserAverages.get("conflict_resolution")))
            .leadership(buildScoreData("leadership", userAverages.get("leadership"), totalUserAverages.get("leadership")))
            .minScore(1)
            .maxScore(5)
            .build();
        
    }

    private ScoreData buildScoreData(String name, Double userScore, Double totalScore) {
        return ScoreData.builder()
            .name(name)
            .score(calculateProgressBarValue(userScore, totalScore))
            .build();
    }

    // 유저별 평균점수 상대위치 계산
    private int calculateProgressBarValue(double userAverage, double totalAverageScore) {
        if (userAverage >= totalAverageScore) {
            // 평균 이상인 경우 (50~100)
            double fraction = (userAverage - totalAverageScore) / (5.0 - totalAverageScore);
            return (int) Math.round(fraction * 50) + 50;
        } else {
            // 평균 이하인 경우 (0~50)
            double fraction = (userAverage - 1.0) / (totalAverageScore - 1.0);
            return (int) Math.round(fraction * 50);
        }
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
            totalScores.merge("problemSolving", projectInfo.getProblemSolving(), Integer::sum);
            totalScores.merge("conflictResolution", projectInfo.getConflictResolution(), Integer::sum);
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

    // 전체 사용자 평균 계산
    private Map<String, Double> getTotalUserAverage() {
        TotalScore totalScore = totalScoreRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("전체 사용자의 점수 데이터가 존재하지 않습니다."));

        int participant = totalScore.getTotalParticipant();

        Map<String, Integer> totalScores = Map.of(
            "sympathy", totalScore.getSympathy().intValue(),
            "listening", totalScore.getListening().intValue(),
            "expression", totalScore.getExpression().intValue(),
            "problem_solving", totalScore.getProblemSolving().intValue(),
            "conflict_resolution", totalScore.getConflictResolution().intValue(),
            "leadership", totalScore.getLeadership().intValue()
        );

        return calculateAverageScores(totalScores, participant);
    }

    private Map<String, Double> calculateAverageScores(Map<String, Integer> totalScores, int participant) {
        Map<String, Double> averageScores = new HashMap<>();
        totalScores.forEach((key, totalScore) -> {
            double average = participant > 0 ? (double) totalScore / participant : 0;
            average = Math.round(average * 10.0) / 10.0;
            averageScores.put(key, average);
        });
        return averageScores;
    }
}


