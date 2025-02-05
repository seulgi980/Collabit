package com.collabit.oauth.service;

import com.collabit.global.security.CustomUserDetails;
import com.collabit.oauth.domain.dto.OAuth2UserRequestDTO;
import com.collabit.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2Service oAuth2Service;

    // 실제 access token을 사용해 GitHub API와 통신하고 사용자 정보를 가져와 DTO에 매핑하는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // OAuth2UserRequest : Access Token, Client 정보 포함
        // OAuth2User : GitHub에서 받은 사용자 정보 가짐
        OAuth2User oauth2User = super.loadUser(oAuth2UserRequest);

        // GitHub에서 받은 사용자 정보를 OAuth2UserRequestDTO에 매핑
        OAuth2UserRequestDTO oauth2UserRequestDTO = OAuth2UserRequestDTO.from(oauth2User);
        log.debug("Loading user {}", oauth2UserRequestDTO.toString());

        User user = oAuth2Service.processOAuth2User(oauth2UserRequestDTO);
        log.debug("Loaded user {}", user.toString());

        // 사용자 속성을 담은 Map 생성
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.put("code", user.getCode());  // 우리 시스템의 고유 식별자 추가

        // Spring Security의 인증 객체로, SecurityContext에 저장하여 TokenProvider에서 JWT 토큰 생성할 때 사용
        return new CustomUserDetails(
                user.getCode(),           // PK
                user.getEmail(),          // Email은 null 가능
                user.getPassword(),       // Password는 null 가능
                user.getNickname(),
                user.getGithubId(),
                user.getProfileImage(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes  // GitHub에서 받아온 원본 정보와 함께 저장
        );
    }
}