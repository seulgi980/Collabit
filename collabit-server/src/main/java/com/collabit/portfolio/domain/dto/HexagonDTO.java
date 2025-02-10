package com.collabit.portfolio.domain.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class HexagonDTO {
    private HexagonDataDTO sympathy;
    private HexagonDataDTO listening;
    private HexagonDataDTO expression;
    private HexagonDataDTO problemSolving;
    private HexagonDataDTO conflictResolution;
    private HexagonDataDTO leadership;

    private int minScore;
    private int maxScore;

}
