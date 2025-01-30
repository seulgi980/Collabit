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

    public List<GetRepositoryResponseDTO> getRepositoryList(String userCode) {
        Optional<User> user = userRepository.findByCode(userCode);

        // 해당 유저의 githubId가 없을 경우 예외처리
        if (user.isEmpty() || user.get().getGithubId().isEmpty()) {
            throw new RuntimeException("Github 연동 회원이 아닙니다.");
        }

        List<GetRepositoryResponseDTO> allRepositories = new ArrayList<>();

        // 개인 레포지토리 조회
        allRepositories.addAll(getUserRepositories(user.get().getGithubId()));

        // organization 레포지토리 조회
        allRepositories.addAll(getOrganizationRepositories(user.get().getGithubId()));

        return allRepositories;
    }

    // 개인 레포지토리 리스트 반환
    private List<GetRepositoryResponseDTO> getUserRepositories(String githubId) {
        String url = String.format("/users/%s/repos", githubId);

        try {
            List<GetRepositoryResponseDTO> repositories = webClient.get()
                    .uri(GITHUB_API_URL + url)
                    .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .map(repo -> GetRepositoryResponseDTO.builder()
                            // .organization((String) ((Map) repo.get("owner")).get("login"))  // 해당 레포지토리 주인의 아이디
                            .organization(githubId)  // 개인 레포지토리의 경우 사용자 이름
                            .title((String) repo.get("name"))
                            .contributorsProfile(new ArrayList<>())
                            .build())
                    .collectList()
                    .block();

            log.info("사용자 {}의 개인 레포지토리 {}개 조회 완료", githubId, repositories.size());
            return repositories;

        } catch (WebClientResponseException e) {
            log.error("GitHub 개인 레포지토리 API 호출 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException("GitHub 개인 레포지토리 리스트를 불러올 수 없습니다.");
        }
    }


    // 사용자가 해당하는 organization 조회 후 각 organization의 레포지토리 리스트 반환
    private List<GetRepositoryResponseDTO> getOrganizationRepositories(String githubId) {
        // 사용자가 속한 조직 목록 조회
        String orgsUrl = String.format("/users/%s/orgs", githubId);
        List<String> organizations;

        try {
            organizations = webClient.get()
                    .uri(GITHUB_API_URL + orgsUrl)
                    .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .map(org -> (String) org.get("login"))
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            log.error("GitHub 조직 목록 API 호출 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException("GitHub organization 리스트를 불러올 수 없습니다.");
        }

        // organizations이 있을 경우 각 조직의 레포지토리 조회
        if (organizations == null || organizations.isEmpty()) {
            return new ArrayList<>();
        }

        List<GetRepositoryResponseDTO> orgRepositories = new ArrayList<>();

        for (String orgName : organizations) {
            try {
                String orgReposUrl = String.format("/orgs/%s/repos", orgName);

                List<GetRepositoryResponseDTO> repos = webClient.get()
                        .uri(GITHUB_API_URL + orgReposUrl)
                        .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                        .retrieve()
                        .bodyToFlux(Map.class)
                        .map(repo -> GetRepositoryResponseDTO.builder()
                                .organization(orgName)  // 조직 레포지토리의 경우 조직 이름
                                .title((String) repo.get("name"))
                                .contributorsProfile(new ArrayList<>())
                                .build())
                        .collectList()
                        .block();

                if(repos != null && !repos.isEmpty()) {
                    orgRepositories.addAll(repos);
                }

            } catch (WebClientResponseException e) {
                log.error("GitHub 조직 레포지토리 API 호출 중 에러 발생 - 조직: {}, 에러: {}", orgName, e.getMessage());
                // 한 조직에서 에러가 발생해도 다른 조직의 레포지토리는 계속 조회해야 하므로 예외 처리X
            }
        }
        return orgRepositories;
    }
}