package com.collabit.oauth.handler;

import com.collabit.auth.domain.dto.TokenDto;
import com.collabit.global.security.TokenProvider;
import com.collabit.oauth.domain.dto.OAuth2UserRequestDTO;
import com.collabit.oauth.domain.enums.OAuth2Status;
import com.collabit.oauth.exception.UnexpectedOAuthStatus;
import com.collabit.oauth.service.OAuth2Service;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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

        // OAuth2SuccessHandler로 넘어올 때 SecurityContext가 초기화되어 SecurityUtil로 새로운 유저 정보 조회 불가
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        OAuth2UserRequestDTO requestDTO = new OAuth2UserRequestDTO(
                oAuth2User.getAttribute("id").toString(),
                oAuth2User.getAttribute("login"),
                oAuth2User.getAttribute("avatar_url")
        );

        User user = oAuth2Service.saveOrLoginOAuth2User(requestDTO);

        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonResponse = new JSONObject();

        switch (oAuth2Status) {
            case GITHUB_LINK_SUCCESS:
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.put("githubId", user.getGithubId());
                break;

            case LOGIN_SUCCESS:
                response.setStatus(HttpServletResponse.SC_OK);
                addAuthTokens(response, authentication);
                jsonResponse.put("nickname", user.getNickname());
                jsonResponse.put("githubId", user.getGithubId());
                break;

            case SIGNUP_SUCCESS:
                response.setStatus(HttpServletResponse.SC_CREATED);
                addAuthTokens(response, authentication);

                jsonResponse.put("nickname", user.getNickname());
                jsonResponse.put("githubId", user.getGithubId());
                break;

            default:
                throw new UnexpectedOAuthStatus();
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