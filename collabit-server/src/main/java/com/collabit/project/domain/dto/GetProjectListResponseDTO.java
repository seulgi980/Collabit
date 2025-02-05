package com.collabit.project.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class GetProjectListResponseDTO {
    private final String organization;
    private final String organizationImage;
    private final List<ProjectDetailDTO> projects;
}
