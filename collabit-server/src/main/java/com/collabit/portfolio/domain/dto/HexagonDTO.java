package com.collabit.portfolio.domain.dto;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class HexagonDTO {
    private List<HexagonDataDTO> skills;
    private int minBaseScore;
    private int maxBaseScore;

}
