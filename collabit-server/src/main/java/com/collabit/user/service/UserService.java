package com.collabit.user.service;

import com.collabit.community.domain.dto.GetPostResponseDTO;
import com.collabit.community.domain.entity.Post;
import com.collabit.community.exception.PostNotFoundException;
import com.collabit.user.domain.dto.GetCurrentUserResponseDTO;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    // 유저 정보 조회하는 메소드
    public GetCurrentUserResponseDTO getCurrentUserInfo(String userCode) {
        Optional<User> user = userRepository.findByCode(userCode);

        if (user.isEmpty()) {
            log.debug("User with code {} not found", userCode);
            throw new UserNotFoundException();
        }

        return GetCurrentUserResponseDTO.builder()
                .githubId(user.get().getGithubId())
                .nickname(user.get().getNickname())
                .profileImage(user.get().getProfileImage())
                .build();
    }
}
