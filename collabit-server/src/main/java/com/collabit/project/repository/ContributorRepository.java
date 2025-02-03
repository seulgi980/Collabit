package com.collabit.project.repository;

import com.collabit.project.domain.dto.ContributorDetailDTO;
import com.collabit.project.domain.entity.Contributor;
import com.github.javaparser.ast.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Integer> {

    Optional<Contributor> findByGithubId(String githubId);

    List<ContributorDetailDTO> findByGithubIdIn(List<String> contributorsGithubId); // 리스트에 해당하는 것의 정보만 반환
}
