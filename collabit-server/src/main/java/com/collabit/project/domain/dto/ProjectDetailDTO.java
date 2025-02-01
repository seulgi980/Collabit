package com.collabit.project.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class ProjectDetailDTO {
    private final int code;
    private final String title;
    private final int participant;
    private final boolean isDone;
    private final Timestamp createdAt;
    private final List<ContributorDetailDTO> contributors;
}
