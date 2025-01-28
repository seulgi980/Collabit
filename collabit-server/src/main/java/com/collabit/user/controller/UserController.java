package com.collabit.user.controller;

import com.collabit.global.security.SecurityUtil;
import com.collabit.user.domain.dto.GetCurrentUserResponseDTO;
import com.collabit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "유저 API")
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary="유저 정보 조회",description = "로그인 시 프론트에서 사용할 유저 정보 반환")
    @GetMapping
    public ResponseEntity<GetCurrentUserResponseDTO> getCurrentUser() {
        log.info("getCurrentUser 메소드 시작");
        String userCode = SecurityUtil.getCurrentUserCode();
        log.debug("조회된 userCode: {}", userCode);
        try {
            GetCurrentUserResponseDTO getCurrentUserResponseDTO = userService.getCurrentUserInfo(userCode);
            log.debug("조회된 유저 정보: {}", getCurrentUserResponseDTO);

            log.info("getCurrentUser 메소드 정상 종료");
            return ResponseEntity.ok(getCurrentUserResponseDTO);

        } catch (Exception e) {
            log.error("유저 정보 조회 중 에러 발생: {}", e.getMessage());
            throw e;
        }
    }


}
