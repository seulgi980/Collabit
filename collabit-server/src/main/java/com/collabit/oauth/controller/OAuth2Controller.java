package com.collabit.oauth.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.oauth.service.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    public OAuth2Controller(OAuth2Service oAuth2Service){
        this.oAuth2Service = oAuth2Service;
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
        String userCode = SecurityUtil.getCurrentUserId();

        if(userCode != null){
            oAuth2Service.storeUserCodeForLinking(userCode);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/oauth2/authorization/github"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        throw new RuntimeException("토큰 정보가 유효하지 않습니다.");
    }
}
