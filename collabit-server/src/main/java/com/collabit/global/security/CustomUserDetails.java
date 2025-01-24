package com.collabit.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    private final String code; // PK
    private final String email; // Email
    private final String password; // Password(인중 후에는 null)
    private final String nickname; // 닉네임
    private final String githubId;
    private final String profileImage; // 프로필 이미지
    private final Collection<? extends GrantedAuthority> authorities;


    @Override
    public String getUsername() {
        return email;
    }



}
