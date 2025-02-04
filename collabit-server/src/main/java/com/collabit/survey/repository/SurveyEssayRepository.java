package com.collabit.survey.repository;

import com.collabit.survey.domain.entity.SurveyEssay;
import com.collabit.survey.domain.entity.SurveyMultiple;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyEssayRepository extends MongoRepository<SurveyEssay, String> {
    SurveyEssay findByProjectInfoCodeAndUserCode(int projectInfoCode, String userCode);
}
