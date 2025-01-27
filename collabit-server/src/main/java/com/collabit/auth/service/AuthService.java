package com.collabit.auth.service;


import com.collabit.auth.domain.dto.TokenDto;
import com.collabit.auth.domain.dto.UserLoginRequestDto;
import com.collabit.auth.domain.dto.UserResponseDto;
import com.collabit.auth.domain.dto.UserSignupRequestDto;
import com.collabit.global.security.CustomUserDetails;
import com.collabit.global.security.TokenProvider;
import com.collabit.user.domain.entity.Role;
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

        // 5. 사용자 정보 조회
        // db에 또 접근하지 말고 Authentication 객체에서 CustomUserDetails 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 6. UserResponseDto 생성 및 반환
        return UserResponseDto.builder()
                .nickname(userDetails.getNickname()) // CustomUserDetails에서 닉네임 가져오기
                .profileImage(userDetails.getProfileImage()) // CustomUserDetails에서 프로필 이미지 가져오기
                .isGithub(userDetails.getGithubId() != null) // CustomUserDetails에서 GitHub 연동 여부 가져오기
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

    // 회원가입 메서드
    public UserResponseDto signup(UserSignupRequestDto userSignupRequestDto) {
        // 이메일 중복 체크
        if (isEmailAlreadyExists(userSignupRequestDto.getEmail())) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        // 닉네임 중복 체크
        if (isNicknameAlreadyExists(userSignupRequestDto.getNickname())) {
            throw new IllegalStateException("이미 사용중인 닉네임입니다.");
        }
        // 비밀번호 암호화
        String encodedPassword = encodePassword(userSignupRequestDto.getPassword());

        // 회원 정보 생성
        User user = User.builder()
                .email(userSignupRequestDto.getEmail())
                .password(encodedPassword)
                .nickname(userSignupRequestDto.getNickname())
                .profileImage("기본이미지주소")
                .role(Role.ROLE_USER)
                .build();

        // 회원 저장
        userRepository.save(user);

        // UserResponseDto 생성(FE 에게 정보 전달 용)
        // isGithub 은 회원가입시에는 false. 로그인 때 확인
        return UserResponseDto.builder()
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .isGithub(false)
                .build();

    }


    // 이메일 중복 체크
    public boolean isEmailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // 닉네임 중복 체크
    public boolean isNicknameAlreadyExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 비밀번호 암호화
    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

}
