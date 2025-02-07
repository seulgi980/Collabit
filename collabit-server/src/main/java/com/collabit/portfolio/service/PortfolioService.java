package com.collabit.portfolio.service;

import com.collabit.global.common.ErrorCode;
import com.collabit.global.error.exception.BusinessException;
import com.collabit.portfolio.domain.dto.HighestLowestSkillDTO;
import com.collabit.portfolio.domain.dto.MultipleAverageByUserResponseDTO;
import com.collabit.portfolio.domain.dto.MultipleAverageDTO;
import com.collabit.portfolio.repository.DescriptionRepository;
import com.collabit.project.domain.entity.ProjectInfo;
import com.collabit.project.repository.ProjectInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final ProjectInfoRepository projectInfoRepository;
    private final DescriptionRepository descriptionRepository;

//
    // 특정 유저가 받은 전체 프로젝트 객관식 6개 영역 평균 조회
    public MultipleAverageByUserResponseDTO averageMultipleByUser(String userCode) {
        // 특정 유저의 전체 프로젝트 조회
        List<ProjectInfo> projectInfos = projectInfoRepository.findByUserCode(userCode);
        log.debug("projectInfos: {}", projectInfos);

        if(projectInfos.isEmpty()){
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }

        List<String> names = descriptionRepository.findDistinctNames();
        log.debug("names: {}", names);

        // 누적할 곳 초기화
        List<Integer> scores = new ArrayList<>();
        for(int i=0; i<names.size(); i++){
            scores.add(0);
        }
        int participant = 0;

        // 총계 누적합 구하기
        for(ProjectInfo projectInfo : projectInfos){
            // 설문지 1개마다 6개 영역 점수, participant 꺼내서 합 모으기
            participant += projectInfo.getParticipant();

            scores.set(0, (scores.get(0) + projectInfo.getSympathy()));
            scores.set(1, (scores.get(1) + projectInfo.getListening()));
            scores.set(2, (scores.get(2) + projectInfo.getExpression()));
            scores.set(3, (scores.get(3) + projectInfo.getProblemSolving()));
            scores.set(4, (scores.get(4) + projectInfo.getConflictResolution()));
            scores.set(5, (scores.get(5) + projectInfo.getLeadership()));
        }
        log.debug("scores: {}", Arrays.toString(scores.toArray()));

        // 계산한 값들 응답형식에 맞추어 저장
        List<MultipleAverageDTO> multipleAverageDtos = new ArrayList<>();

        for(int i=0; i<names.size(); i++){
            double roundedScore = Math.round(((double) scores.get(i)  / participant) * 10.0) / 10.0;
            MultipleAverageDTO dto = MultipleAverageDTO.builder()
                    .name(names.get(i))
                    .score(roundedScore)
                    .build();
            multipleAverageDtos.add(dto);
        }
        log.debug("multipleAverageDtos: {}", multipleAverageDtos);


        int maxIdx = 0;
        int minIdx = 0;

        for(int i=0; i<scores.size(); i++){
            if(scores.get(i) > scores.get(maxIdx)){
                maxIdx = i;
            }
            if(scores.get(i) < scores.get(minIdx)){
                minIdx = i;
            }
        }
        // 최고,최저역량 이름
        String highestSkillName = names.get(maxIdx);
        String lowestSkillName = names.get(minIdx);

        // 최고,최저역량 설명
        String highestSkillDesc = descriptionRepository.findByNameAndId_IsPositive(highestSkillName, true).getDescription();
        String lowestSkillDesc = descriptionRepository.findByNameAndId_IsPositive(lowestSkillName, false).getDescription();

        HighestLowestSkillDTO.Skill highestSkill  = new HighestLowestSkillDTO.Skill(highestSkillName, highestSkillDesc);
        HighestLowestSkillDTO.Skill lowestSkill  = new HighestLowestSkillDTO.Skill(lowestSkillName, lowestSkillDesc);
        log.debug("highestSkill: {}", highestSkill.toString());
        log.debug("lowestSkill: {}", lowestSkill.toString());

        HighestLowestSkillDTO highestLowestSkillDTO = HighestLowestSkillDTO.builder()
                .highestSkill(highestSkill)
                .lowestSkill(lowestSkill)
                .build();

        return MultipleAverageByUserResponseDTO
                .builder()
                .scores(multipleAverageDtos)
                .highestLowestSkills(highestLowestSkillDTO)
                .build();
    }
}
