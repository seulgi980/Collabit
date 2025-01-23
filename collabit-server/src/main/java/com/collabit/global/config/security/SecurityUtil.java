package com.collabit.global.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Spring Security의 SecurityContext 에 저장된 현재 사용자(로그인된 사용자)의 ID를 가져오는 유틸리티 메서드
@Slf4j
public class SecurityUtil {
    private SecurityUtil() {} // 클래스 내부 static메서드만 이용하도록 private 생성자

    // 현재 요청의 SecurityContext에 저장된 인증 정보를 가져와, 인증된 사용자의 ID를 반환
    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null)  {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }
}
