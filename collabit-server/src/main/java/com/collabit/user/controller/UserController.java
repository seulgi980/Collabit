package com.collabit.user.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.user.domain.dto.GetCurrentUserResponseDTO;
import com.collabit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "유저 API")
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary="유저 정보 조회",description = "로그인 시 프론트에서 사용할 유저 정보 반환")
    @GetMapping
    public ResponseEntity<GetCurrentUserResponseDTO> getCurrentUser() {
        String userCode = SecurityUtil.getCurrentUserCode();

        GetCurrentUserResponseDTO getCurrentUserResponseDTO = userService.getCurrentUserInfo(userCode);

        return ResponseEntity.ok(getCurrentUserResponseDTO);
    }


}
