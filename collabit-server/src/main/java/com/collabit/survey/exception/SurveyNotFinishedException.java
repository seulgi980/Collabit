package com.collabit.survey.exception;

public class SurveyNotFinishedException extends RuntimeException {
    public SurveyNotFinishedException() {
        super("아직 설문에 참여하지 않았습니다.");
    }
}
