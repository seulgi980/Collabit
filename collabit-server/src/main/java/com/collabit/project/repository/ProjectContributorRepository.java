package com.collabit.project.repository;

import com.collabit.project.domain.entity.ProjectContributor;
import com.collabit.project.domain.entity.ProjectContributorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectContributorRepository extends JpaRepository<ProjectContributor, ProjectContributorId> {
}
