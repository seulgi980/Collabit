package com.collabit.global.security;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    private String grantType;             // 토큰 유형 (Bearer)
    private String accessToken;           // Access Token
    private Long accessTokenExpiresIn;    // Access Token 만료 시간 (Unix Time)
    private String refreshToken;          // Refresh Token
}

