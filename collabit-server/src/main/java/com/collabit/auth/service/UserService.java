package com.collabit.auth.service;

import com.collabit.auth.controller.dto.UserSignupRequestDto;
import com.collabit.auth.controller.dto.UserResponseDto;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        return new UserResponseDto(user.getNickname(), user.getProfileImage(), false);

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
