package com.collabit.project.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@RequiredArgsConstructor
public class CreateProjectRequestDTO {
    private String title;
    private String organization;
    private String organizationImage;
    private List<ContributorDetailDTO> contributors;
}
