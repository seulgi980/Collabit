package com.collabit.project.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.project.domain.dto.CreateProjectRequestDTO;
import com.collabit.project.domain.dto.GetProjectListResponseDTO;
import com.collabit.project.domain.entity.SortOrder;
import com.collabit.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
    public ResponseEntity<?> getProjectList(@RequestParam(required = false) String keyword, @RequestParam(required = false, defaultValue = "LATEST") SortOrder sort) {
        String userCode = SecurityUtil.getCurrentUserCode();

        List<GetProjectListResponseDTO> projectList = projectService.findProjectList(userCode, keyword, sort);
        log.info("프로젝트 목록 데이터 반환 - 반환할 프로젝트 수: {}", projectList.size());

        return ResponseEntity.ok(projectList);
    }

    // 쿼리 파라미터로 들어온 정렬 조건이 올바르지 않을 때 커스텀 예외 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleInvalidEnumValue(Exception e) {
        return ResponseEntity.badRequest().body("정렬 조건이 올바르지 않습니다. 가능한 조건: LATEST, PARTICIPATION");
    }
}
