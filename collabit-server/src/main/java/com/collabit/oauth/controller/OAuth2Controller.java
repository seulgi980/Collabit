package com.collabit.oauth.controller;

import com.collabit.oauth.exception.GithubAlreadyLinkedException;
import com.collabit.oauth.service.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "OAuth2", description = "OAuth2 인증 관련 API")
@RequestMapping("/api/oauth")
@RestController
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
//    private final JwtService jwtService;

    public OAuth2Controller(OAuth2Service oAuth2Service){ //, JwtService jwtService) {
        this.oAuth2Service = oAuth2Service;
//        this.jwtService = jwtService;
    }

    @Operation(summary = "깃허브 로그인, 회원가입")
    @GetMapping
    public ResponseEntity<String> getGithubAccount(){
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/github"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Operation(summary = "일반 회원의 깃허브 연동 요청")
    @GetMapping("/link")
    public ResponseEntity<String> linkGithub(HttpServletRequest request) {
        // HTTP Only 쿠키에서 JWT 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {  // JWT 토큰을 저장한 쿠키 이름
                    String token = cookie.getValue();
                    try {
//                        String userCode = jwtService.extractUserCode(token);
                        String userCode = "test123";
                        oAuth2Service.storeUserCodeForLinking(userCode);

                        HttpHeaders headers = new HttpHeaders();
                        headers.setLocation(URI.create("/oauth2/authorization/github"));
                        return new ResponseEntity<>(headers, HttpStatus.FOUND);
                    } catch (Exception e) {
                        throw new GithubAlreadyLinkedException("유효하지 않은 토큰입니다.");
                    }
                }
            }
        }
        throw new GithubAlreadyLinkedException("로그인이 필요합니다.");
    }
}
