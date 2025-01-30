package com.collabit.project.service;

import com.collabit.project.domain.dto.ContributorDetailDTO;
import com.collabit.project.domain.dto.GetRepositoryResponseDTO;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class GithubAPIService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    private static final String GITHUB_API_URL = "https://api.github.com";

    // 개인, organization 레포지토리 정보를 각각 조회하여 합쳐 리스트로 반환
    public List<GetRepositoryResponseDTO> getRepositoryList(String userCode) {
        Optional<User> user = userRepository.findByCode(userCode);

        // 해당 유저의 githubId가 없을 경우 예외처리
        if (user.isEmpty() || user.get().getGithubId().isEmpty()) {
            throw new RuntimeException("Github 연동 회원이 아닙니다.");
        }

        List<GetRepositoryResponseDTO> allRepositories = new ArrayList<>();
        String githubId = user.get().getGithubId();

        // 개인 레포지토리 조회
        allRepositories.addAll(getUserRepositories(githubId));

        // organization 레포지토리 조회
        allRepositories.addAll(getOrganizationRepositories(githubId));

        return allRepositories;
    }

    // contributor의 프로필 이미지 URL 리스트를 가져오는 메서드
    private Mono<List<String>> getContributorsProfileUrl(String owner, String repoName) {
        String url = String.format("/repos/%s/%s/contributors", owner, repoName);

            return webClient.get()
                    .uri(GITHUB_API_URL + url)
                    .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                    .retrieve()
                    .onStatus(status -> status.value() == 404,
                            error -> Mono.empty()) // 404 에러(contributor 없음)의 경우 빈 결과 반환
                    .bodyToFlux(Map.class)
                    .map(contributor -> (String) contributor.get("avatar_url"))
                    .collectList()
                    .onErrorResume(WebClientResponseException.class, e -> {
                        log.error("GitHub Contributors API 호출 중 에러 발생 - 레포지토리: {}/{}, 에러: {}",
                                owner, repoName, e.getMessage());
                        // return Mono.just(new ArrayList<>()); // 에러 발생 시 예외처리하지 않고 빈 리스트 반환하여 다른 리스트에 대해서 다 불러올지?
                        throw new RuntimeException("GitHub Contributors의 프로필 이미지를 불러올 수 없습니다.");
                    });
    }

    // 개인 레포지토리 리스트 반환
    private List<GetRepositoryResponseDTO> getUserRepositories(String githubId) {
        String url = String.format("/users/%s/repos", githubId);

        try {
            return webClient.get()
                    .uri(GITHUB_API_URL + url)
                    .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .flatMap(repo -> {
                        String repoName = (String) repo.get("name");
                        return getContributorsProfileUrl(githubId, repoName)
                                .map(contributorsProfile -> GetRepositoryResponseDTO.builder()
                                        .organization(githubId)
                                        .title(repoName)
                                        .contributorsProfile(contributorsProfile)
                                        .timestamp(Timestamp.valueOf(((String) repo.get("updated_at"))
                                                .replace("T", " ")
                                                .replace("Z", "")))  // GitHub API의 시간 형식을 Timestamp 형식으로 변환
                                        .build());
                    })
                    .collectList()
                    .doOnSuccess(repositories -> {
                        log.info("사용자 {}의 개인 레포지토리 {}개 조회 완료", githubId, repositories.size());
                    })
                    .block();

        } catch (WebClientResponseException e) {
            log.error("GitHub 개인 레포지토리 API 호출 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException("GitHub 개인 레포지토리 리스트를 불러올 수 없습니다.");
        }
    }

    // 사용자가 소속된 organization 조회 후 각 organization의 레포지토리 리스트 반환
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
            log.error("GitHub organization 목록 API 호출 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException("GitHub organization 리스트를 불러올 수 없습니다.");
        }

        // organizations이 있을 경우 각 organization의 레포지토리 조회
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
                        .flatMap(repo -> {
                            String repoName = (String) repo.get("name");
                            return getContributorsProfileUrl(orgName, repoName)
                                    .map(contributorsProfile -> GetRepositoryResponseDTO.builder()
                                            .organization(orgName)
                                            .title(repoName)
                                            .contributorsProfile(contributorsProfile)
                                            .timestamp(Timestamp.valueOf(((String) repo.get("updated_at"))
                                                    .replace("T", " ")
                                                    .replace("Z", "")))
                                            .build());
                        })
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

    // 특정 레포지토리의 컨트리뷰터 상세 정보를 조회하는 메서드
    public List<ContributorDetailDTO> getRepositoryContributors(String organization, String repository) {
        String url = String.format("/repos/%s/%s/contributors", organization, repository); // 개인 레포지토리도 organization에 본인의 아이디가 들어가므로 상관X

        log.info("GitHub API 호출 시작 - organization: {}, repository: {}", organization, repository);

        try {
            List<ContributorDetailDTO> contributors = webClient.get()
                    .uri(GITHUB_API_URL + url)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .map(contributor -> {
                        String githubId = (String) contributor.get("login");
                        String profileImage = (String) contributor.get("avatar_url");

                        log.debug("컨트리뷰터 정보 매핑 - githubId: {}", githubId);

                        return ContributorDetailDTO.builder()
                                .githubId(githubId)
                                .profileImage(profileImage)
                                .build();
                    })
                    .collectList()
                    .block();

            log.info("GitHub API 호출 완료 - 컨트리뷰터 수: {}", contributors.size());
            return contributors;

        } catch (WebClientResponseException e) {
            log.error("GitHub Contributors API 호출 중 에러 발생 - 레포지토리: {}/{}, 에러: {}",
                    organization, repository, e.getMessage());
            throw new RuntimeException("GitHub Contributors 정보를 불러올 수 없습니다.");
        }
    }
}