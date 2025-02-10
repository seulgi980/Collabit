package com.collabit.portfolio.domain.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class ScoreDTO {
    private String name;
    private double score;
}
