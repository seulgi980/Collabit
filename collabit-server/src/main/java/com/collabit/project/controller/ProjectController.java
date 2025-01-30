package com.collabit.project.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.project.domain.dto.GetRepositoryResponseDTO;
import com.collabit.project.service.GithubAPIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ProjectController", description = "프로젝트 API")
@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final GithubAPIService githubAPIService;

    @Operation(summary = "깃허브 레포지토리 조회", description = "user의 githubId로 조직명, 레포지토리명, 컨트리뷰터 프로필 이미지를 리스트로 받는 API입니다.")
    @PostMapping("/repo")
    public ResponseEntity<?> getGithubRepository() {
        String userCode = SecurityUtil.getCurrentUserCode();

        List<GetRepositoryResponseDTO> repositoryList = githubAPIService.getRepositoryList(userCode);

        return ResponseEntity.ok(repositoryList);
    }

}
