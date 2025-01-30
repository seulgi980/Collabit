package com.collabit.project.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.project.domain.dto.CreateProjectRequestDTO;
import com.collabit.project.domain.dto.GetRepositoryResponseDTO;
import com.collabit.project.service.GithubAPIService;
import com.collabit.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProjectController", description = "프로젝트 API")
@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final GithubAPIService githubAPIService;
    private final ProjectService projectService;

    @Operation(summary = "깃허브 레포지토리 조회", description = "user의 githubId로 조직명, 레포지토리명, 컨트리뷰터 프로필 이미지를 리스트로 받는 API입니다.")
    @GetMapping("/repo")
    public ResponseEntity<?> getGithubRepository() {
        String userCode = SecurityUtil.getCurrentUserCode();

        List<GetRepositoryResponseDTO> repositoryList = githubAPIService.getRepositoryList(userCode);

        return ResponseEntity.ok(repositoryList);
    }

    @Operation(summary = "프로젝트 등록", description = "프로젝트 정보를 등록하는 API 입니다.")
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody CreateProjectRequestDTO createProjectRequestDTO) {
        String userCode = SecurityUtil.getCurrentUserCode();

        projectService.createProject(createProjectRequestDTO, userCode);

        return ResponseEntity.status(201).build();
    }

}
