package com.collabit.portfolio.domain.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class HexagonDataDTO {
    private String name;
    private double score;
    private double total;
    private String feedback;
    private String description;
    private boolean isPositive;
}
