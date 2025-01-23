package com.collabit.user.handler;

import com.collabit.user.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Service oAuth2Service;

    public OAuth2SuccessHandler(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2Service.AuthStatus authStatus = oAuth2Service.getAuthStatus();
        response.setContentType("application/json;charset=UTF-8");

        switch (authStatus) {
            case GITHUB_LINK_SUCCESS:
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("GitHub 계정 연동이 완료되었습니다.");
                break;

            case LOGIN_SUCCESS:
                // TODO: JWT 토큰 생성 및 쿠키에 저장 로직 추가 필요

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("로그인이 완료되었습니다.");
                break;

            case SIGNUP_SUCCESS:
                // TODO: JWT 토큰 생성 및 쿠키에 저장 로직 추가 필요
                
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("회원가입이 완료되었습니다.");
                break;

            default:
                throw new IllegalStateException("Unexpected auth status");
        }
        oAuth2Service.clearAuthStatus(); // 인증 상태 초기화
    }
}