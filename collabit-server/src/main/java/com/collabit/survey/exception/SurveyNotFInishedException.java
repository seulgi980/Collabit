package com.collabit.survey.exception;

public class SurveyNotFInishedException extends RuntimeException {
    public SurveyNotFInishedException() {
        super("아직 설문에 참여하지 않았습니다.");
    }
}
