package com.collabit.project.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.project.domain.dto.CreateProjectRequestDTO;
import com.collabit.project.domain.dto.GetProjectListResponseDTO;
import com.collabit.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProjectController", description = "프로젝트 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 등록", description = "프로젝트 정보를 등록하는 API 입니다.")
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody CreateProjectRequestDTO createProjectRequestDTO) {
        String userCode = SecurityUtil.getCurrentUserCode();

        log.info("프로젝트 등록 서비스 전달 - CreateProjectRequestDTO: {}, userCode: {}", createProjectRequestDTO.toString(), userCode);
        projectService.saveProject(createProjectRequestDTO, userCode);

        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록을 조회하는 API 입니다.")
    @GetMapping
    public ResponseEntity<?> getProjectList() {
        String userCode = SecurityUtil.getCurrentUserCode();

        List<GetProjectListResponseDTO> projectList = projectService.findProjectList(userCode);
        log.info("프로젝트 목록 데이터 반환 - 반환할 프로젝트 수: {}", projectList.size());

        return ResponseEntity.ok(projectList);
    }
}
