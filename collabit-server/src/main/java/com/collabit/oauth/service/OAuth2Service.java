package com.collabit.oauth.service;

import com.collabit.global.security.SecurityUtil;
import com.collabit.oauth.domain.dto.OAuth2UserRequestDTO;
import com.collabit.oauth.domain.enums.OAuth2Status;
import com.collabit.oauth.exception.GithubAlreadyLinkedException;
import com.collabit.oauth.exception.GithubAlreadyUsedException;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class OAuth2Service {
    private final UserRepository userRepository;

    @Getter
    private OAuth2Status oAuth2Status;

    // SecurityUtil에서 인증 정보가 없는 경우 바로 예외를 던짐
    // 토큰이 없을 때는 연동 요청 제한, 토큰이 있을 때는 로그인/회원가입 요청 제한 (security 설정)
    // 해당 메소드에서는 usercode의 유무로 로그인/회원가입과 연동을 구분하기에 로그인/회원가입의 경우 무조건 예외 발생
    // => 예외가 발생하는 경우는 무조건 로그인/회원가입으로 처리
    public User processOAuth2User(OAuth2UserRequestDTO oauth2UserRequestDTO) {
        String userCode = null;
        try {
            userCode = SecurityUtil.getCurrentUserCode();
            linkGithubAccount(userCode, oauth2UserRequestDTO);
            return userRepository.findByCode(userCode)
                    .orElseThrow(UserNotFoundException::new);
        } catch (Exception e) {
            if(userCode == null) {
                return saveOrLoginOAuth2User(oauth2UserRequestDTO);
            }
            throw e;
        }
    }

    // GitHub ID로 기존 사용자 찾아서 있으면 로그인, 없으면 회원가입
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
                .email(null)
                .password(null)
                .githubId(oAuth2UserRequestDTO.getGithubId())
                .nickname(oAuth2UserRequestDTO.getNickname())
                .profileImage(oAuth2UserRequestDTO.getProfileImage())
                .role(Role.ROLE_USER) // 기본 사용자 권한 설정
                .build();

        return userRepository.save(newGitUser);
    }

    // 일반회원이 GitHub 계정을 연동할 경우 해당 유저 레코드에 깃허브 계정 추가
    public void linkGithubAccount(String userCode, OAuth2UserRequestDTO oAuth2UserRequestDTO) {
        User user = userRepository.findByCode(userCode)
                .orElseThrow(UserNotFoundException::new);

        // 이미 GitHub 계정이 연동된 경우
        if (user.getGithubId() != null) {
            throw new GithubAlreadyLinkedException();
        }

        // GitHub ID가 이미 다른 계정에 연동된 경우 체크
        if (userRepository.existsByGithubId(oAuth2UserRequestDTO.getGithubId())) {
            throw new GithubAlreadyUsedException();
        }

        user.linkGithub(oAuth2UserRequestDTO.getGithubId());
        userRepository.save(user);
        oAuth2Status = OAuth2Status.GITHUB_LINK_SUCCESS;
    }
}
