package com.collabit.project.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreateProjectRequestDTO {
    private String organization;
    private String title;
    private List<ContributorDetailDTO> contributors;
}
