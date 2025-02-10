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
    private Map<String, HexagonDataDTO> hexagonData;
    private int minScore;
    private int maxScore;

}
