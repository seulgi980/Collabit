package com.collabit.auth.controller;

import com.collabit.auth.domain.dto.UserLoginRequestDto;
import com.collabit.auth.domain.dto.UserResponseDto;
import com.collabit.auth.domain.dto.UserSignupRequestDto;
import com.collabit.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "Auth(로그인, 회원가입) 관련 API")
public class AuthController {
    private final AuthService authService;


    // 회원가입
    @Operation(summary = "일반 회원가입", description = "일반 사이트 자체 회원가입 하는 API입니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserSignupRequestDto userSignupRequestDTO) {
        log.debug("signUp Request: {}", userSignupRequestDTO.toString());
        UserResponseDto userResponseDto = authService.signup(userSignupRequestDTO);
        log.debug("signUp Response: {}", userResponseDto.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }


    // 로그인
    @Operation(summary = "일반 로그인", description = "일반 사이트 자체 로그인 하는 API입니다." )
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        log.debug("login Request: {}", userLoginRequestDto.toString());
        return ResponseEntity.ok(authService.login(userLoginRequestDto, response));
    }


}
