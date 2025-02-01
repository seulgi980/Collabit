package com.collabit.project.repository;

import com.collabit.project.domain.entity.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Integer> {

    List<ProjectInfo> findByUserCode(String userCode);

    ProjectInfo findByProjectCodeAndUserCode(int code, String userCode);

}
