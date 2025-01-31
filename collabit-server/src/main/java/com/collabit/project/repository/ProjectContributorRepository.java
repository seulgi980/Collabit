package com.collabit.project.repository;

import com.collabit.project.domain.entity.ProjectContributor;
import com.collabit.project.domain.entity.ProjectContributorId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectContributorRepository extends JpaRepository<ProjectContributor, ProjectContributorId> {

    List<ProjectContributor> findByProjectCodeAndProjectInfoCode(int projectCode, int projectInfoCode);

    // project_code가 같고, project_info_code가 현재 코드보다 작거나 같은 모든 contributor 조회
    @Query("SELECT pc FROM ProjectContributor pc " +
            "WHERE pc.project.code = :projectCode " +
            "AND pc.projectInfo.code <= :currentProjectInfoCode")
    List<ProjectContributor> findByProjectCodeAndProjectInfoCodeLessThanEqual(
            @Param("projectCode") Integer projectCode,
            @Param("currentProjectInfoCode") Integer currentProjectInfoCode);
}
