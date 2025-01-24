package com.collabit.oauth.handler;

import com.collabit.auth.domain.dto.TokenDto;
import com.collabit.global.security.TokenProvider;
import com.collabit.oauth.service.OAuth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Service oAuth2Service;
    private final TokenProvider tokenProvider;

    // Oauth2 로그인, 회원가입 성공 시 token 발급 및 성공 메시지 전달
    // 연동 성공 시 메시지만 전달
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2Service.AuthStatus authStatus = oAuth2Service.getAuthStatus(); // 로그인, 회원가입, 연동 구분
        response.setContentType("application/json;charset=UTF-8");

        String message;
        switch (authStatus) {
            case GITHUB_LINK_SUCCESS:
                response.setStatus(HttpServletResponse.SC_OK);
                message = "GitHub 계정 연동이 완료되었습니다.";
                break;

            case LOGIN_SUCCESS:
                TokenDto loginTokenDto = tokenProvider.generateTokenDto(authentication);
                addCookie(response, "accessToken", loginTokenDto.getAccessToken(),
                        tokenProvider.getAccessTokenExpireTime() / 1000);
                addCookie(response, "refreshToken", loginTokenDto.getRefreshToken(),
                        tokenProvider.getRefreshTokenExpireTime() / 1000);
                response.setStatus(HttpServletResponse.SC_OK);
                message = "로그인이 완료되었습니다.";
                break;

            case SIGNUP_SUCCESS:
                TokenDto signupTokenDto = tokenProvider.generateTokenDto(authentication);
                addCookie(response, "accessToken", signupTokenDto.getAccessToken(),
                        tokenProvider.getAccessTokenExpireTime() / 1000);
                addCookie(response, "refreshToken", signupTokenDto.getRefreshToken(),
                        tokenProvider.getRefreshTokenExpireTime() / 1000);
                response.setStatus(HttpServletResponse.SC_CREATED);
                message = "회원가입이 완료되었습니다.";
                break;

            default:
                throw new IllegalStateException("Unexpected auth status");
        }

        // JSONObject에 데이터를 추가하고 문자열로 변환
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("message", message);
        response.getWriter().write(jsonResponse.toString());

        oAuth2Service.clearAuthStatus();
    }

    private void addCookie(HttpServletResponse response, String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge);
        response.addCookie(cookie);
    }
}