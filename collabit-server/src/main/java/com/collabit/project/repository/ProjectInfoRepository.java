package com.collabit.project.repository;

import com.collabit.project.domain.entity.ProjectInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Integer> {

    List<ProjectInfo> findByUserCode(String userCode);

    ProjectInfo findByProjectCodeAndUserCode(int code, String userCode);

    @Query("SELECT DISTINCT pi FROM ProjectInfo pi " +
            "JOIN FETCH pi.project p " +
            "WHERE pi.user.code = :userCode")
    List<ProjectInfo> findByUserCodeWithProject(@Param("userCode") String userCode);
}
