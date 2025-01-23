package com.collabit.auth.controller.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;             // 토큰 유형 (Bearer)
    private String accessToken;           // Access Token
    private Long accessTokenExpiresIn;    // Access Token 만료 시간
    private String refreshToken;          // Refresh Token
}
