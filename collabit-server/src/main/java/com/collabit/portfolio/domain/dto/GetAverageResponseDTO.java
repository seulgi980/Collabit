package com.collabit.portfolio.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAverageResponseDTO {
    private double sympathy;
    private double listening;
    private double expression;
    private double problemSolving;
    private double conflictResolution;
    private double leadership;
}