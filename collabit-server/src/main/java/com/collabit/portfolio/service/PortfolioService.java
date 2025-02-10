package com.collabit.portfolio.service;

import com.collabit.global.common.ErrorCode;
import com.collabit.global.error.exception.BusinessException;
import com.collabit.portfolio.domain.dto.*;
import com.collabit.portfolio.domain.entity.Portfolio;
import com.collabit.portfolio.domain.entity.Description;
import com.collabit.portfolio.domain.entity.Feedback;
import com.collabit.portfolio.domain.entity.Portfolio;
import com.collabit.portfolio.repository.DescriptionRepository;
import com.collabit.portfolio.repository.FeedbackRepository;
import com.collabit.portfolio.repository.PortfolioRepository;
import com.collabit.project.domain.entity.ProjectInfo;
import com.collabit.project.domain.entity.TotalScore;
import com.collabit.project.repository.ProjectInfoRepository;
import com.collabit.project.repository.ProjectRepository;
import com.collabit.project.repository.TotalScoreRepository;
import com.collabit.project.service.ProjectService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final ProjectRepository projectRepository;
    private final ProjectInfoRepository projectInfoRepository;
    private final DescriptionRepository descriptionRepository;
    private final FeedbackRepository feedbackRepository;
    private final TotalScoreRepository totalScoreRepository;
    private final ProjectService projectService;
    private final PortfolioRepository portfolioRepository;

    private final int MIN_PEOPLE_COUNT = 6;

    public GetMultipleHexagonProgressResponseDTO getHexagonAndProgressbarGraph(String userCode) {

        // 개인 역량별 평균 계산
        Map<String, Double> userAverages = getUserAverage(userCode);

        // 전체 사용자의 역량별 평균 계산
        Map<String, Double> totalUserAverages = getTotalUserAverage();

        // 각 항목에 대해 개인 평균, 전체 평균 비교하여 isPositive만 세팅
        Map<String, Boolean> isAboveAverageBySkill = getSkillAboveAverageMap(userAverages, totalUserAverages);

        // Description 데이터 Map 변환
        Map<String, Description> descriptionMap = descriptionRepository.findAll().stream()
            .collect(Collectors.toMap(Description::getCode, desc -> desc));

        // Feedback 데이터 Map 변환
        Map<String, List<Feedback>> feedbackMap = feedbackRepository.findAll().stream()
            .collect(Collectors.groupingBy(Feedback::getCode));

        // List<SkillData>를 Map으로 변환하여 필드명에 맞게 매핑
        Map<String, SkillData> skillDataMap = userAverages.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    // 각 code와 isPositive에 맞는 설명, 피드백을 매핑
                    String code = entry.getKey();
                    Description description = descriptionMap.get(code);
                    boolean isPositive = isAboveAverageBySkill.get(code);

                    String feedback = feedbackMap.get(code).stream()
                        .filter(f -> f.isPositive() == isPositive)
                        .findFirst()
                        .map(Feedback::getFeedback)
                        .orElse("");

                    return SkillData.builder()
                        .score(entry.getValue())
                        .name(description.getName())
                        .description(description.getDescription())
                        .feedback(feedback)
                        .isPositive(isPositive)
                        .build();
                }
            ));

        GetHexagonResponseDTO hexagon = GetHexagonResponseDTO.builder()
            .minScore(1)
            .maxScore(5)
            .sympathy(skillDataMap.get("sympathy"))
            .listening(skillDataMap.get("listening"))
            .expression(skillDataMap.get("expression"))
            .problemSolving(skillDataMap.get("problem_solving"))
            .conflictResolution(skillDataMap.get("conflict_resolution"))
            .leadership(skillDataMap.get("leadership"))
            .build();

        GetProgressResponseDTO progress = GetProgressResponseDTO.builder()
            .sympathy(buildScoreData("sympathy", userAverages.get("sympathy"), totalUserAverages.get("sympathy")))
            .listening(buildScoreData("listening", userAverages.get("listening"), totalUserAverages.get("listening")))
            .expression(buildScoreData("expression", userAverages.get("expression"), totalUserAverages.get("expression")))
            .problemSolving(buildScoreData("problemSolving", userAverages.get("problem_solving"), totalUserAverages.get("problem_solving")))
            .conflictResolution(buildScoreData("conflictResolution", userAverages.get("conflict_resolution"), totalUserAverages.get("conflict_resolution")))
            .leadership(buildScoreData("leadership", userAverages.get("leadership"), totalUserAverages.get("leadership")))
            .minScore(1)
            .maxScore(5)
            .build();

        return GetMultipleHexagonProgressResponseDTO.builder()
            .hexagon(hexagon)
            .progress(progress)
            .build();
    }

    private Map<String, Boolean> getSkillAboveAverageMap(Map<String, Double> userAverages, Map<String, Double> totalUserAverages){
        Map<String, Boolean> isAboveAverageMap = new HashMap<>();
        userAverages.forEach((key, personalScore) -> {
            if (personalScore >= totalUserAverages.get(key)) {
                isAboveAverageMap.put(key, true);
            }
            else{
                isAboveAverageMap.put(key, false);
            }
        });
        return isAboveAverageMap;
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

    // 프로젝트 기간별 비교 그래프 데이터 조회
    public GetTimelineResponseDTO getTimelineGraph(String userCode) {
        // 현재 로그인된 유저의 설문이 마감된 최근 8개 projectInfo 조회
        List<ProjectInfo> projectInfoList = projectInfoRepository.findTop8ByUserCodeAndCompletedAtIsNotNullOrderByCompletedAtDesc(userCode);

        if(projectInfoList == null || projectInfoList.isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_INFO_NOT_FOUND);
        }

        List<TimelineData> timelineDataList = new ArrayList<>();

        // 각 projectInfo의 항목별 객관식 점수 구하기
        for (ProjectInfo projectInfo : projectInfoList) {
            Map<String, ScoreData> nameAndScores = projectService.getProjectInfoAverageWithName(projectInfo.getCode());

            TimelineData timelineData = TimelineData.builder()
                    .projectName(projectInfo.getProject().getTitle())
                    .organization(projectInfo.getProject().getOrganization())
                    .completedAt(projectInfo.getCompletedAt())
                    .sympathy(nameAndScores.get("sympathy"))
                    .listening(nameAndScores.get("listening"))
                    .expression(nameAndScores.get("expression"))
                    .problemSolving(nameAndScores.get("problem_solving"))
                    .conflictResolution(nameAndScores.get("conflict_resolution"))
                    .leadership(nameAndScores.get("leadership"))
                    .build();

            timelineDataList.add(timelineData);
        }

        return GetTimelineResponseDTO.builder()
                .timeline(timelineDataList)
                .minScore(1)
                .maxScore(5)
                .build();
    }

    // 포트폴리오 상태 (갱신 가능 여부, 포트폴리오 존재 여부, 포트폴리오 참여자 수) 조회
    public GetPortfolioStatusResponseDTO getPortfolioStatus(String userCode) {
        // 포트폴리오 존재 여부
        Portfolio portfolio = portfolioRepository.findById(userCode).orElse(null);

        // 포트폴리오 참여자 수
        List<ProjectInfo> completedProjectList = projectInfoRepository.findByUser_CodeAndCompletedAtIsNotNull(userCode);
        int totalParticipant = calTotalParticipant(completedProjectList);

        // 포트폴리오 갱신 가능 여부
        boolean isUpdate = canUpdatePortfolio(portfolio, totalParticipant);

        return GetPortfolioStatusResponseDTO.builder()
                .isUpdate(isUpdate)
                .isExist(portfolio!=null)
                .totalParticipant(totalParticipant)
                .build();
    }

    // 포트폴리오 기본 정보(포트폴리오 대상 닉네임, 참여자 수, 프로젝트 수) 조회
    public GetPortfolioInfoResponseDTO getPortfolioInfo(String userCode) {
        Portfolio portfolio = portfolioRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("아직 포트폴리오가 생성되지 않았습니다."));

        return GetPortfolioInfoResponseDTO.builder()
                .nickname(portfolio.getUser().getNickname())
                .participant(portfolio.getParticipant())
                .project(portfolio.getProject())
                .build();
    }

    // 프로젝트 완료 시 갱신 여부 업데이트
    public void enablePortfolioUpdate(String userCode) {
        // 포트폴리오 존재 여부
        Portfolio portfolio = portfolioRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("아직 포트폴리오가 생성되지 않았습니다."));

        // 해당 유저의 마감된 projectInfo 리스트
        List<ProjectInfo> completedProjectList = projectInfoRepository.findByUser_CodeAndCompletedAtIsNotNull(userCode);
        int totalParticipant = calTotalParticipant(completedProjectList);

        // 포트폴리오 갱신 가능 여부
        boolean isUpdate = canUpdatePortfolio(portfolio, totalParticipant);

        if (isUpdate) {
            portfolio.changeUpdateStatus();
            portfolioRepository.save(portfolio);
        }
    }

    private int calTotalParticipant(List<ProjectInfo> completedProjectList){
        return completedProjectList.stream()
                .mapToInt(ProjectInfo::getParticipant)
                .sum();
    }

    private boolean canUpdatePortfolio(Portfolio portfolio, int participant) {
        boolean isUpdate = false;
        // 포트폴리오가 아직 생성 전이면 6명 이상인지 확인
        if(portfolio == null && participant >= MIN_PEOPLE_COUNT){
            isUpdate = true;
        }
        // 포트폴리오가 이미 생성되었다면 포트폴리오 테이블의 isUpdate도 확인
        else if(portfolio != null && portfolio.getIsUpdate() && participant >= MIN_PEOPLE_COUNT){
            isUpdate = true;
        }
        return isUpdate;
    }

    @Transactional
    public void generatePortfolio(String userCode) {
        Portfolio portfolio = portfolioRepository.findByUserCode(userCode)
            .orElse(new Portfolio());

        List<ProjectInfo> newProjectInfos = projectInfoRepository.findAllByUserCodeAndCompletedAtAfter(
            userCode,
            portfolio.getUpdatedAt() != null ? portfolio.getUpdatedAt() : LocalDateTime.MIN
        );

        if (!newProjectInfos.isEmpty()) {
            Map<String, Long> totalScores = new HashMap<>();
            int totalProjects = portfolio.getProject() + newProjectInfos.size();
            int totalParticipants = portfolio.getParticipant();

            for (ProjectInfo info : newProjectInfos) {
                totalScores.merge("sympathy", (long) info.getSympathy(), Long::sum);
                totalScores.merge("listening", (long) info.getListening(), Long::sum);
                totalScores.merge("expression", (long) info.getExpression(), Long::sum);
                totalScores.merge("problemSolving", (long) info.getProblemSolving(), Long::sum);
                totalScores.merge("conflictResolution", (long) info.getConflictResolution(), Long::sum);
                totalScores.merge("leadership", (long) info.getLeadership(), Long::sum);
                totalParticipants += info.getParticipant();
            }

            portfolio.update(
                userCode,
                totalProjects,
                totalParticipants,
                totalScores.getOrDefault("sympathy", 0L),
                totalScores.getOrDefault("listening", 0L),
                totalScores.getOrDefault("expression", 0L),
                totalScores.getOrDefault("problemSolving", 0L),
                totalScores.getOrDefault("conflictResolution", 0L),
                totalScores.getOrDefault("leadership", 0L),
                LocalDateTime.now()
            );

            portfolioRepository.save(portfolio);
        }
    }

    public GetAverageResponseDTO getAverage() {
        Map<String, Double> averages = getTotalUserAverage();

        return GetAverageResponseDTO.builder()
            .sympathy(averages.get("sympathy"))
            .listening(averages.get("listening"))
            .expression(averages.get("expression"))
            .problemSolving(averages.get("problem_solving"))
            .conflictResolution(averages.get("conflict_resolution"))
            .leadership(averages.get("leadership"))
            .build();
    }
}


