package com.collabit.portfolio.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
public class GetRecommendedPostResponseDTO {
    String userNickname;
    String userImageUrl;
    String content;
    int commentCount;
    int likesCount;
}
