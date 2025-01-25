package com.collabit.oauth.service;

import com.collabit.global.security.SecurityUtil;
import com.collabit.oauth.domain.dto.OAuth2UserRequestDTO;
import com.collabit.oauth.domain.enums.OAuth2Status;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OAuth2Service {
    private final UserRepository userRepository;

    @Getter
    private OAuth2Status oAuth2Status;
    
    // token에서 usercode를 가져와 회원가입/로그인인지 연동인지 구분 후 해당하는 메소드 호출
    @Transactional
    public void processOAuth2User(OAuth2UserRequestDTO oauth2UserRequestDTO) {
        String userCode = SecurityUtil.getCurrentUserId();

        if (userCode != null) {
            linkGithubAccount(userCode, oauth2UserRequestDTO);
        } else {
            saveOrLoginOAuth2User(oauth2UserRequestDTO);
        }
    }

    // GitHub ID로 기존 사용자 찾아서 있으면 로그인, 없으면 회원가입
    @Transactional
    public User saveOrLoginOAuth2User(OAuth2UserRequestDTO oAuth2UserRequestDTO) {
        return userRepository.findByGithubId(oAuth2UserRequestDTO.getGithubId())
                // 해당 GitHub Id가 있으면 로그인 처리
                .map(user -> {
                    oAuth2Status = OAuth2Status.LOGIN_SUCCESS;
                    return user;
                })
                // 없으면 회원가입 처리
                .orElseGet(() -> {
                    oAuth2Status = OAuth2Status.SIGNUP_SUCCESS;
                    return signUpOAuth2User(oAuth2UserRequestDTO);
                });
    }

    // 새로운 GitHub 사용자 생성 및 저장
    private User signUpOAuth2User(OAuth2UserRequestDTO oAuth2UserRequestDTO) {
        User newGitUser = User.builder()
                .code(UUID.randomUUID().toString()) // 고유한 사용자 코드 생성
                .githubId(oAuth2UserRequestDTO.getGithubId())
                .nickname(oAuth2UserRequestDTO.getNickname())
                .profileImage(oAuth2UserRequestDTO.getProfileImage())
                .role(Role.ROLE_USER) // 기본 사용자 권한 설정
                .build();

        return userRepository.save(newGitUser);
    }

    // 일반회원이 GitHub 계정을 연동할 경우 해당 유저 레코드에 깃허브 계정 추가
    @Transactional
    public void linkGithubAccount(String userCode, OAuth2UserRequestDTO oAuth2UserRequestDTO) {
        User user = userRepository.findByCode(userCode)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 이미 GitHub 계정이 연동된 경우
        if (user.getGithubId() != null) {
            throw new RuntimeException("이미 GitHub 계정이 연동되어 있습니다.");
        }

        // GitHub ID가 이미 다른 계정에 연동된 경우 체크
        if (userRepository.existsByGithubId(oAuth2UserRequestDTO.getGithubId())) {
            throw new RuntimeException("이미 다른 계정에 연동된 GitHub 계정입니다.");
        }

        user.linkGithub(oAuth2UserRequestDTO.getGithubId());
        userRepository.save(user);
        oAuth2Status = OAuth2Status.GITHUB_LINK_SUCCESS;
    }

    // SecurityUtil을 통해 usercode를 가져오고 GitHub Id 전달
    @Transactional(readOnly = true)
    public Object getUserGithubId() {
        return userRepository.findByCode(SecurityUtil.getCurrentUserId())
                .map(User::getGithubId)
                .orElse(null);
    }

    // SecurityUtil을 통해 usercode를 가져오고 닉네임 전달
    @Transactional(readOnly = true)
    public Object getUserNickname() {
        return userRepository.findByCode(SecurityUtil.getCurrentUserId())
                .map(User::getNickname)
                .orElse(null);
    }
}
