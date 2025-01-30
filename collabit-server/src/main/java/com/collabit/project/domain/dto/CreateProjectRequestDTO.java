package com.collabit.project.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateProjectRequestDTO {
    private String organization;
    private String title;
}
