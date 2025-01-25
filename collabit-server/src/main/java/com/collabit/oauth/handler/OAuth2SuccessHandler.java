package com.collabit.oauth.handler;

import com.collabit.auth.domain.dto.TokenDto;
import com.collabit.global.security.TokenProvider;
import com.collabit.oauth.domain.enums.OAuth2Status;
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

    // Oauth2 로그인, 회원가입 성공 시 token 발급 및 상태코드 전달
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2Status oAuth2Status = oAuth2Service.getOAuth2Status(); // 로그인, 회원가입, 연동 구분
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonResponse = new JSONObject();

        switch (oAuth2Status) {
            case GITHUB_LINK_SUCCESS:
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("githubId", oAuth2Service.getUserGithubId());
                break;

            case LOGIN_SUCCESS:
                response.setStatus(HttpServletResponse.SC_OK);
                addAuthTokens(response, authentication);
                jsonResponse.put("nickname", oAuth2Service.getUserNickname());
                jsonResponse.put("githubId", oAuth2Service.getUserGithubId());
                break;

            case SIGNUP_SUCCESS:
                response.setStatus(HttpServletResponse.SC_CREATED);
                addAuthTokens(response, authentication);
                jsonResponse.put("nickname", oAuth2Service.getUserNickname());
                jsonResponse.put("githubId", oAuth2Service.getUserGithubId());
                break;

            default:
                throw new IllegalStateException("GitHub 인증으로 수행할 수 없습니다.");
        }

        jsonResponse.put("status", response.getStatus());
        response.getWriter().write(jsonResponse.toString());
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