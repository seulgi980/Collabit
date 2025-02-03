package com.collabit.survey.repository;

import com.collabit.survey.domain.entity.SurveyResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResponseRepository extends MongoRepository<SurveyResponse, String> {
    // projectInfoCode(1)에 대해 특정 유저(A)가 평가한 내역 조회(평가를 했는지 확인하는 용도)
    List<SurveyResponse> findByProjectInfoCodeAndUserCode(int projectInfoCode, String userCode);
}
