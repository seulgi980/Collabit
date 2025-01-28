package com.collabit.global.security;

import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.debug("Loading user by user email {}", userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.debug("User not found for email: {}", userEmail);
                    return new UserNotFoundException();
                });

        return new CustomUserDetails(
                user.getCode(), // PK
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getGithubId(),
                user.getProfileImage(),
                user.getAuthorities() // 권한 정보
        );
    }
}

