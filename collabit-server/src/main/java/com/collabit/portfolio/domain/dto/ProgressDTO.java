package com.collabit.portfolio.domain.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class ProgressDTO {
    private List<ProgressDataDTO> scores;
    private int min;
    private int max;
}


