package com.collabit.portfolio.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class getMultipleHexagonProgressResponseDTO {
    private getHexagonResponseDTO hexagon;
    private getProgressResponseDTO progress;
}
