package com.collabit.project.service;

import com.collabit.project.domain.dto.GetRepositoryResponseDTO;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    private static final String GITHUB_API_URL = "https://api.github.com";

    public List<String> getRepositoryList(String userCode) {
        Optional<User> user = userRepository.findByCode(userCode);

        // 해당 유저의 githubId가 없을 경우 예외처리
        if (user.isEmpty() || user.get().getGithubId().isEmpty()) {
            throw new RuntimeException("Github 연동 회원이 아닙니다.");
        }

        String url = String.format("/users/%s/repos", user.get().getGithubId());

        try {
            return webClient.get()
                    .uri(GITHUB_API_URL + url)
                    .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .map(repo -> (String) repo.get("name"))
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            log.error("GitHub API 호출 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException("GitHub 레포지토리 조회 실패");
        }
    }
}