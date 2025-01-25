package com.collabit.oauth.controller;

import com.collabit.auth.exception.InvalidTokenException;
import com.collabit.global.security.SecurityUtil;
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

    // 토큰이 있는 사용자의 경우 접근이 불가능하도록 security config에 추가
    @Operation(summary = "깃허브 로그인, 회원가입")
    @GetMapping
    public ResponseEntity<String> getGithubAccount(){
        return redirectToGithub();
    }

    @Operation(summary = "일반 회원의 깃허브 연동 요청")
    @GetMapping("/link")
    public ResponseEntity<String> linkGithub(HttpServletRequest request) {
        String userCode = SecurityUtil.getCurrentUserId();

        if(userCode == null){
            throw new InvalidTokenException();
        }

        return redirectToGithub();
    }

    // 사용자 동의를 위한 GitHub 페이지로 redirect 하는 메소드 
    private ResponseEntity<String> redirectToGithub() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/github"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
