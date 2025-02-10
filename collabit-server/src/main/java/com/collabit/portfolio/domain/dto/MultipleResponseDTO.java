package com.collabit.portfolio.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class MultipleResponseDTO {
    private HexagonDTO hexagon;
    private ProgressDTO progress;
}
