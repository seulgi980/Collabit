package com.collabit.auth.domain.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TokenDto {
    private String grantType;             // 토큰 유형 (Bearer)
    private String accessToken;           // Access Token
    private Long accessTokenExpiresIn;    // Access Token 만료 시간
    private String refreshToken;          // Refresh Token
}
