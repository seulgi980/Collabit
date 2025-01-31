package com.collabit.project.service;

import com.collabit.project.domain.dto.ContributorDetailDTO;
import com.collabit.project.domain.dto.CreateProjectRequestDTO;
import com.collabit.project.domain.dto.GetProjectListResponseDTO;
import com.collabit.project.domain.entity.*;
import com.collabit.project.repository.ContributorRepository;
import com.collabit.project.repository.ProjectContributorRepository;
import com.collabit.project.repository.ProjectInfoRepository;
import com.collabit.project.repository.ProjectRepository;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectInfoRepository projectInfoRepository;
    private final ContributorRepository contributorRepository;
    private final ProjectContributorRepository projectContributorRepository;
    private final UserRepository userRepository;

    // Github API로 레포지토리 contributor 정보 가져와서 프로젝트 정보들과 함께 저장
    public void saveProject(CreateProjectRequestDTO createProjectRequestDTO, String userCode) {

    }

    // 로그인 유저의 전체 프로젝트 조회
    @Transactional(readOnly = true)
    public List<GetProjectListResponseDTO> findProjectList(String userCode) {
        log.info("프로젝트 목록 조회 시작 - userCode: {}", userCode);

        // 1. userCode로 ProjectInfo 리스트 조회
        List<ProjectInfo> projectInfoList = projectInfoRepository.findByUserCode(userCode);
        log.debug("사용자의 ProjectInfo 조회 완료 - 조회된 ProjectInfo 수: {}", projectInfoList.size());

        // 2. ProjectInfo를 기반으로 Project 정보와 Contributor 정보를 조회 후 DTO 매핑
        List<GetProjectListResponseDTO> result = projectInfoList.stream()
                .map(projectInfo -> {
                    Project project = projectInfo.getProject();
                    log.debug("Project 정보 조회 - projectCode: {}, title: {}, organization: {}",
                            project.getCode(), project.getTitle(), project.getOrganizationName());

                    // 3. projectContributorRepository(인덱싱 테이블)를 통해 프로젝트의 contributor 목록 조회
                    List<ProjectContributor> projectContributors = projectContributorRepository
                            .findByProjectCodeAndProjectInfoCodeLessThanEqual(
                                    project.getCode(),
                                    projectInfo.getCode()
                            );
                    log.debug("ProjectContributor 조회 완료 - projectCode: {}, currentProjectInfoCode: {}, contributorCount: {}",
                            project.getCode(), projectInfo.getCode(), projectContributors.size());

                    List<ContributorDetailDTO> contributors = projectContributors.stream()
                            .map(projectContributor -> {
                                Contributor contributor = projectContributor.getContributor();
                                log.trace("Contributor 정보 변환 - githubId: {}", contributor.getGithubId());
                                return ContributorDetailDTO.builder()
                                        .githubId(contributor.getGithubId())
                                        .profileImage(contributor.getProfileImage())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    // 4. DTO 생성
                    GetProjectListResponseDTO getProjectListResponseDTO = GetProjectListResponseDTO.builder()
                            .code(projectInfo.getCode())
                            .organization(project.getOrganizationName())
                            .title(project.getTitle())
                            .total(projectInfo.getTotal())
                            .participant(projectInfo.getParticipant())
                            .isDone(projectInfo.isDone())
                            .contributors(contributors)
                            .build();

                    log.debug("프로젝트 DTO 생성 완료 - projectInfoCode: {}, contributorCount: {}",
                            projectInfo.getCode(), contributors.size());

                    return getProjectListResponseDTO;
                })
                .collect(Collectors.toList());

        log.info("프로젝트 목록 조회 완료 - 조회된 프로젝트 수: {}", result.size());
        return result;
    }
}
