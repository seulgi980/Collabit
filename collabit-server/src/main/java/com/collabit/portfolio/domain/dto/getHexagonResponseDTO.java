package com.collabit.portfolio.domain.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class getHexagonResponseDTO {
    private HexagonData sympathy;
    private HexagonData listening;
    private HexagonData expression;
    private HexagonData problemSolving;
    private HexagonData conflictResolution;
    private HexagonData leadership;

    private int minScore;
    private int maxScore;

}
