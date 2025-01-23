package com.collabit.auth.controller;

import com.collabit.auth.controller.dto.*;
import com.collabit.auth.service.AuthService;
import com.collabit.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    // 회원가입
    @PostMapping("/api/user/sign-up")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserSignupRequestDto userSignupRequestDto){
        return ResponseEntity.ok(userService.signup(userSignupRequestDto));
    }

    // 로그인
    @PostMapping("/api/auth/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(userLoginRequestDto, response));
    }


}
