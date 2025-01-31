package com.collabit.project.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class GetProjectListResponseDTO {
    private final int code;
    private final String organization;
    private final String title;
    private final int total;
    private final int participant;
    private final boolean isDone;
    private final List<ContributorDetailDTO> contributors;
}
