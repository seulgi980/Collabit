package com.collabit.project.repository;

import com.collabit.project.domain.entity.Project;
import com.collabit.project.domain.entity.ProjectContributor;
import com.collabit.project.domain.entity.ProjectContributorId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectContributorRepository extends JpaRepository<ProjectContributor, ProjectContributorId> {

    // project로 ProjectContributor 리스트 조회
    List<ProjectContributor> findByProject(Project project);

    // project_code가 같고, project_info_code가 현재 코드보다 작거나 같은 모든 contributor 조회
    @Query("SELECT pc.id.githubId FROM ProjectContributor pc " +
            "WHERE pc.id.projectCode = :projectCode " +
            "AND pc.id.projectInfoCode <= :currentProjectInfoCode")
    List<String> findByProjectCodeAndProjectInfoCodeLessThanEqual(
            @Param("projectCode") int projectCode,
            @Param("currentProjectInfoCode") int currentProjectInfoCode);

    // projectInfo 코드로 ProjectContributor 리스트 조회
    List<ProjectContributor> findByProjectInfoCode(int code);

    void deleteByProjectCode(int code);

    void deleteByProjectInfoCode(int code);

    List<ProjectContributor> findByIdGithubId(String githubId);
}
