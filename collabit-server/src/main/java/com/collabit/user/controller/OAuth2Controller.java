package com.collabit.user.controller;

import com.collabit.user.service.GithubAlreadyLinkedException;
import com.collabit.user.service.OAuth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
//    private final JwtService jwtService;

    public OAuth2Controller(OAuth2Service oAuth2Service){ //, JwtService jwtService) {
        this.oAuth2Service = oAuth2Service;
//        this.jwtService = jwtService;
    }

    @GetMapping("/api/oauth")
    public String getGithubAccount(){
        return "redirect:/oauth2/authorization/github";
    }

    // 일반 회원의 깃허브 연동 요청
    @GetMapping("/api/oauth/link")
    public String linkGithub(HttpServletRequest request) {
        // HTTP Only 쿠키에서 JWT 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {  // JWT 토큰을 저장한 쿠키 이름
                    String token = cookie.getValue();
                    try {
//                        String userCode = jwtService.extractUserCode(token);
                        String userCode = "user123";
                        oAuth2Service.storeUserCodeForLinking(userCode);
                        return "redirect:/oauth2/authorization/github";
                    } catch (Exception e) {
                        throw new GithubAlreadyLinkedException("유효하지 않은 토큰입니다.");
                    }
                }
            }
        }
        throw new GithubAlreadyLinkedException("로그인이 필요합니다.");
    }
}
