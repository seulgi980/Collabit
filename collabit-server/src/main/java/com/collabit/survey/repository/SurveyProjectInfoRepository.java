package com.collabit.survey.repository;

import com.collabit.project.domain.entity.ProjectInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SurveyProjectInfoRepository extends CrudRepository<ProjectInfo, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE ProjectInfo p SET p.sympathy = p.sympathy + :sympathy, " +
            "p.listening = p.listening + :listening, " +
            "p.expression = p.expression + :expression, " +
            "p.problemSolving = p.problemSolving + :problemSolving, " +
            "p.conflictResolution = p.conflictResolution + :conflictResolution, " +
            "p.leadership = p.leadership + :leadership " +
            "WHERE p.code = :projectInfoCode")
    void updateSurveyScores(int projectInfoCode, int sympathy, int listening, int expression,
                            int problemSolving, int conflictResolution, int leadership);
}

