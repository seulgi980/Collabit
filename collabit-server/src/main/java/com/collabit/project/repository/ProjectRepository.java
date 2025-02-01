package com.collabit.project.repository;

import com.collabit.project.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Project findByTitleAndOrganization(String title, String title1);
}
