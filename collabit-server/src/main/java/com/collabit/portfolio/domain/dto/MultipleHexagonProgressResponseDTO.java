package com.collabit.portfolio.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class MultipleHexagonProgressResponseDTO {
    private HexagonDTO hexagon;
    private ProgressDTO progress;
}
