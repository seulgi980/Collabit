package com.collabit.project.service;

import com.collabit.project.domain.dto.ContributorDetailDTO;
import com.collabit.project.domain.dto.CreateProjectRequestDTO;
import com.collabit.project.domain.dto.GetProjectListResponseDTO;
import com.collabit.project.domain.dto.ProjectDetailDTO;
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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    // 프론트에서 받은 프로젝트 정보 검증 후 프로젝트 저장
    public void saveProject(CreateProjectRequestDTO createProjectRequestDTO, String userCode) {
        log.info("프로젝트 등록 시작 - CreateProjectRequestDTO: {}, userCode: {}", createProjectRequestDTO.toString(), userCode);

        // 1. 시용자 조회
        User user = userRepository.findByCode(userCode)
                .orElseThrow(UserNotFoundException::new);
        log.debug("사용자 조회 완료 - userCode: {}", userCode);

        // 2. Project가 없는 경우 저장
        Project project = projectRepository.findByTitleAndOrganization(
                createProjectRequestDTO.getTitle(),
                createProjectRequestDTO.getOrganization());

        if (project == null) {
            project = Project.builder()
                    .title(createProjectRequestDTO.getTitle())
                    .organization(createProjectRequestDTO.getOrganization())
                    .organizationImage(createProjectRequestDTO.getOrganizationImage())
                    .build();
            project = projectRepository.save(project);
            log.debug("새 프로젝트 저장 완료 - projectCode: {}", project.getCode());
        }
        else {
            log.debug("기존 프로젝트 발견 - projectCode: {}, title: {}, organization: {}",
                    project.getCode(), project.getTitle(), project.getOrganization());
        }

        // 3. ProjectInfo 저장
        // 현재 로그인 유저가 해당 레포지토리를 저장한적 있는지 검증 (projectCode, userCode로 조회)
        ProjectInfo existingProjectInfo = projectInfoRepository
                    .findByProjectCodeAndUserCode(project.getCode(), userCode);

        if(existingProjectInfo != null) {
            log.warn("이미 등록된 프로젝트 정보 발견 - projectCode: {}, projectInfoCode: {}, userCode: {}",
                    project.getCode(), existingProjectInfo.getCode(), userCode);
            throw new RuntimeException("이미 등록하신 레포지토리입니다.");
        }

        // 등록되지 않은 레포지토리의 정보를 ProjectInfo에 저장
        ProjectInfo projectInfo = ProjectInfo.builder()
                .project(project)
                .user(user)
                .total(createProjectRequestDTO.getContributors().size())
                .build();
        projectInfo = projectInfoRepository.save(projectInfo);
        log.debug("ProjectInfo 저장 완료 - projectInfoCode: {}", projectInfo.getCode());

        // 4. Contributor 처리
        // 다른 유저가 이미 저장했던 레포지토리를 저장할 경우 동일한 contributor 정보 사용
        // -> 중복 저장하지 않기 위해 해당 project에 저장되어 있는 contributor 정보 조회
        List<ProjectContributor> existingContributors = projectContributorRepository
                .findByProject(project);
        log.debug("기존 컨트리뷰터 조회 완료 - projectCode: {}, 기존 컨트리뷰터 수: {}",
                project.getCode(), existingContributors.size());

        // 프론트에서 받아온 contributor와 저장된 contributor를 비교하여 중복되지 않은 contributor만 저장
        for(ContributorDetailDTO contributorDetailDTO : createProjectRequestDTO.getContributors()) {
            boolean exists = existingContributors.stream()
                    .anyMatch(pc -> pc.getId().getGithubId().equals(contributorDetailDTO.getGithubId()));

            // 현재 DB에 해당 project 소속으로 없는 contributor인 경우
            if(!exists) {
                // Contributor 테이블에서 조회 (다른 프로젝트의 contributor로 등록되어 있을 수 있음)
                Contributor contributor = contributorRepository
                        .findByGithubId(contributorDetailDTO.getGithubId())
                        .orElse(null);

                // 다른 프로젝트에도 소속되어 있지 않는 경우 contributor 저장
                if(contributor == null) {
                    contributor = Contributor.builder()
                            .githubId(contributorDetailDTO.getGithubId())
                            .profileImage(contributorDetailDTO.getProfileImage())
                            .build();
                    contributor = contributorRepository.save(contributor);
                    log.debug("새 컨트리뷰터 저장 - githubId: {}", contributor.getGithubId());
                } else {
                    log.debug("기존 컨트리뷰터 발견 - githubId: {}", contributor.getGithubId());
                }

                // 해당 project 소속으로 없는 contributor이므로 ProjectContributor에 관계 저장
                ProjectContributorId projectContributorId = new ProjectContributorId(
                        project.getCode(),
                        projectInfo.getCode(),
                        contributor.getGithubId()
                );

                ProjectContributor projectContributor = ProjectContributor.builder()
                        .id(projectContributorId)
                        .project(project)
                        .projectInfo(projectInfo)
                        .contributor(contributor)
                        .build();

                projectContributorRepository.save(projectContributor);
                log.debug("ProjectContributor 관계 저장 완료 - projectCode: {}, projectInfoCode: {}, githubId: {}",
                        project.getCode(), projectInfo.getCode(), contributor.getGithubId());
            }
        }

        log.info("프로젝트 등록 완료 - projectCode: {}, projectInfoCode: {}, 컨트리뷰터 수: {}",
                project.getCode(), projectInfo.getCode(), createProjectRequestDTO.getContributors().size());
    }

    // 로그인 유저의 전체 프로젝트 조회
    @Transactional(readOnly = true)
    public List<GetProjectListResponseDTO> findProjectList(String userCode) {
        log.info("프로젝트 목록 조회 시작 - userCode: {}", userCode);

        // 1. 로그인 유저의 ProjectInfo 리스트 조회
        // project와 함께 조회하여 N+1 문제 방지 (후에 project 테이블에 있는 정보 조회 시 발생)
        List<ProjectInfo> projectInfoList = projectInfoRepository.findByUserCodeWithProject(userCode);
        log.debug("사용자의 ProjectInfo 조회 완료 - 조회된 ProjectInfo 수: {}", projectInfoList.size());

        // 2. organization별로 그룹핑
        Map<String, List<ProjectInfo>> groupedByOrg = projectInfoList.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProject().getOrganization()));

        // 2. organizaion으로 묶은 ProjectInfo 리스트를 기반으로 Project 정보와 Contributor 정보를 조회 후 DTO 매핑
        List<GetProjectListResponseDTO> result = groupedByOrg.entrySet().stream()
                .map(entry -> {
                    String org = entry.getKey();
                    List<ProjectInfo> orgProjects = entry.getValue();
                    Project firstProject = orgProjects.get(0).getProject(); // 그룹내 organization은 모두 동일하여 1개만 저장

                    List<ProjectDetailDTO> projects = orgProjects.stream()
                            .map(projectInfo -> {
                                Project project = projectInfo.getProject();

                                // contributor 정보 조회 (같은 project의 해당 projectInfo 이전의 모든 contributor 조회)
                                List<String> contributorsGithubId = projectContributorRepository
                                        .findByProjectCodeAndProjectInfoCodeLessThanEqual(
                                                project.getCode(),
                                                projectInfo.getCode()
                                        );

                                // 조회한 contributor들의 githubId, 프로필 이미지 조회
                                List<ContributorDetailDTO> contributors = contributorRepository
                                        .findByGithubIdIn(contributorsGithubId)
                                        .stream()
                                        .map(contributor -> ContributorDetailDTO.builder()
                                                .githubId(contributor.getGithubId())
                                                .profileImage(contributor.getProfileImage())
                                                .build())
                                        .collect(Collectors.toList());

                                return ProjectDetailDTO.builder()
                                        .code(projectInfo.getCode())
                                        .title(project.getTitle())
                                        .participant(projectInfo.getParticipant())
                                        .isDone(projectInfo.isDone())
                                        .createdAt(projectInfo.getCreateAt())
                                        .contributors(contributors)
                                        .build();
                            })

                            // isDone=false인 것이 앞에 오도록 정렬, isDone이 같을 때 code 내림차순 정렬
                            .sorted(Comparator
                                    .comparing(ProjectDetailDTO::isDone)
                                    .thenComparing(ProjectDetailDTO::getCode, Comparator.reverseOrder()))
                            .collect(Collectors.toList());

                    return GetProjectListResponseDTO.builder()
                            .organization(org)
                            .organizationImage(firstProject.getOrganizationImage())
                            .projects(projects)
                            .build();
                })
                .collect(Collectors.toList());

        log.info("프로젝트 목록 조회 완료 - 조회된 organization 수: {}", result.size());
        return result;
    }
}
