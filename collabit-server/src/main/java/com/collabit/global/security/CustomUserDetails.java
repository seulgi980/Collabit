package com.collabit.global.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Setter
public class CustomUserDetails implements UserDetails, OAuth2User {
    private final String code; // PK
    private final String email; // Email
    private final String password; // Password(인중 후에는 null)
    private final String nickname; // 닉네임
    private final String githubId;
    private final String profileImage; // 프로필 이미지
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes; // OAuth2User를 위한 필드 추가

    @Override
    public String getUsername() {
        return code;
    }

    // 기본 생성자
    public CustomUserDetails(String code, String email, String password, String nickname,
                             String githubId, String profileImage,
                             Collection<? extends GrantedAuthority> authorities) {
        this.code = code;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.githubId = githubId;
        this.profileImage = profileImage;
        this.authorities = authorities;
        this.attributes = new HashMap<>();
    }

    // OAuth2User용 생성자
    public CustomUserDetails(String code, String email, String password, String nickname,
                             String githubId, String profileImage,
                             Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> attributes) {
        this(code, email, password, nickname, githubId, profileImage, authorities);
        this.attributes = attributes;
    }

    // OAuth2User 인터페이스 구현
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.code; // OAuth2User의 getName()은 유저의 고유 식별자를 반환
    }

}
