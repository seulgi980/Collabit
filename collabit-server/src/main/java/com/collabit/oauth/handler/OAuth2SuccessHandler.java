package com.collabit.oauth.handler;

import com.collabit.auth.domain.dto.TokenDto;
import com.collabit.global.security.TokenProvider;
import com.collabit.oauth.domain.enums.OAuth2Status;
import com.collabit.oauth.service.OAuth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Service oAuth2Service;
    private final TokenProvider tokenProvider;

    @Value("${oauth.frontend.redirect-url}")
    private String frontendRedirectUrl;

    // Oauth2 로그인, 회원가입 성공 시 token 발급 및 상태코드 전달
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2Status oAuth2Status = oAuth2Service.getOAuth2Status(); // 로그인, 회원가입, 연동 구분
        log.debug("OAuth2 status is {}", oAuth2Status);

        // 로그인/회원가입 성공시에만 토큰 발급
        if (oAuth2Status != OAuth2Status.GITHUB_LINK_SUCCESS) {
            addAuthTokens(response, authentication);
        }

        // 프론트엔드로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, frontendRedirectUrl);
    }

    // 인증 정보로부터 토큰을 생성하여 쿠키에 저장
    private void addAuthTokens(HttpServletResponse response, Authentication authentication) {
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        addCookie(response, "accessToken", tokenDto.getAccessToken(),
                tokenProvider.getAccessTokenExpireTime() / 1000);

        addCookie(response, "refreshToken", tokenDto.getRefreshToken(),
                tokenProvider.getRefreshTokenExpireTime() / 1000);
    }

    // 쿠키 설정(이름, 값, 유효기간, 보안옵션)을 생성하고 응답에 추가
    private void addCookie(HttpServletResponse response, String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge);
        response.addCookie(cookie);
    }
}