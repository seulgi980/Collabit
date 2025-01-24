package com.collabit.user.controller;

import com.collabit.auth.domain.dto.UserSignupRequestDto;
import com.collabit.auth.domain.dto.UserResponseDto;
import com.collabit.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserSignupRequestDto userSignupRequestDTO) {
        UserResponseDto userResponseDto = userService.signup(userSignupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }
}
