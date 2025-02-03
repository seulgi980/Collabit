package com.collabit.survey.repository;

import com.collabit.survey.domain.entity.SurveyQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyQuestionRepository extends MongoRepository<SurveyQuestion, String> {
}
