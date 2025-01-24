package com.collabit.auth.service;


import com.collabit.auth.domain.dto.TokenDto;
import com.collabit.auth.domain.dto.UserLoginRequestDto;
import com.collabit.auth.domain.dto.UserResponseDto;
import com.collabit.global.security.TokenProvider;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto login(UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        // 1. Login ID/PW를 기반으로 AuthenticationToken 생성
        String email = userLoginRequestDto.getEmail();
        String password = userLoginRequestDto.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 사용자 인증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. HttpOnly Cookie 에 Access Token 과 Refresh Token 저장
        addCookie(response, "accessToken", tokenDto.getAccessToken(), tokenProvider.getAccessTokenExpireTime() / 1000);
        addCookie(response, "refreshToken", tokenDto.getRefreshToken(), tokenProvider.getRefreshTokenExpireTime() / 1000);

        // 5. 데이터베이스에서 사용자 정보 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 6. UserResponseDto 생성 및 반환
        return UserResponseDto.builder()
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .isGithub(user.getGithubId() != null)
                .build();

    }

    // HttpOnly Cookie 에 토큰 저장
    public void addCookie(HttpServletResponse response, String name, String value, long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // HttpOnly 설정
        cookie.setSecure(true); // HTTPS 에서만 동작
        cookie.setPath("/"); // 쿠키가 유효한 경로
        cookie.setMaxAge((int) maxAge); // 만료 시간 (초 단위)
        response.addCookie(cookie); // 클라이언트로 쿠키 전송 필수
    }
}
