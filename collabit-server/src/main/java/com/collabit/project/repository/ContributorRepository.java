package com.collabit.project.repository;

import com.collabit.project.domain.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Integer> {

    Optional<Contributor> findByGithubId(String githubId);
}
