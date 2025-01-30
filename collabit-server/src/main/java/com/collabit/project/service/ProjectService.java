package com.collabit.project.service;

import com.collabit.project.domain.dto.ContributorDetailDTO;
import com.collabit.project.domain.dto.CreateProjectRequestDTO;
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
    private final GithubAPIService githubAPIService;

    public void createProject(CreateProjectRequestDTO createProjectRequestDTO, String userCode) {
        log.info("프로젝트 생성 시작 - organization: {}, title: {}, userCode: {}",
                createProjectRequestDTO.getOrganization(), createProjectRequestDTO.getTitle(), userCode);

        // 1. 사용자 조회
        User user = userRepository.findByCode(userCode)
                .orElseThrow(UserNotFoundException::new);
        log.debug("사용자 조회 완료 - githubId: {}", user.getGithubId());

        // 2. GitHub API를 통해 컨트리뷰터 정보 조회
        List<ContributorDetailDTO> contributors = githubAPIService.getRepositoryContributors(
                createProjectRequestDTO.getOrganization(),
                createProjectRequestDTO.getTitle()
        );
        log.debug("컨트리뷰터 정보 조회 완료 - 수: {}", contributors.size());

        // 3. Project 엔티티 저장
        Project project = Project.builder()
                .title(createProjectRequestDTO.getTitle())
                .organizationName(createProjectRequestDTO.getOrganization())
                .build();
        projectRepository.save(project);
        log.debug("Project 엔티티 저장 완료 - code: {}", project.getCode());

        // 4. ProjectInfo 엔티티 저장
        ProjectInfo projectInfo = ProjectInfo.builder()
                .project(project)
                .user(user)
                .total(contributors.size())
                .build();
        projectInfoRepository.save(projectInfo);
        log.debug("ProjectInfo 엔티티 저장 완료 - code: {}", projectInfo.getCode());

        // 5. Contributor 정보 저장
        contributors.forEach(contributorDTO -> {
            Contributor contributor = contributorRepository.findByGithubId(contributorDTO.getGithubId())
                    .orElseGet(() -> {
                        Contributor newContributor = Contributor.builder()
                                .githubId(contributorDTO.getGithubId())
                                .profileImage(contributorDTO.getProfileImage())
                                .build();
                        log.debug("새로운 Contributor 생성 - githubId: {}", contributorDTO.getGithubId());
                        return contributorRepository.save(newContributor);
                    });

            // 복합키 객체 생성
            ProjectContributorId projectContributorId = new ProjectContributorId(
                    project.getCode(),
                    projectInfo.getCode(),
                    contributor.getCode()
            );

            ProjectContributor projectContributor = ProjectContributor.builder()
                    .id(projectContributorId)
                    .project(project)
                    .projectInfo(projectInfo)
                    .contributor(contributor)
                    .build();
            projectContributorRepository.save(projectContributor);
            log.debug("ProjectContributor 연결 완료 - githubId: {}", contributor.getGithubId());
        });
        log.info("프로젝트 생성 완료 - projectCode: {}", project.getCode());
    }
}
