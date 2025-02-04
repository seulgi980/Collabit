package com.collabit.survey.exception;

public class SurveyMessageDecodingException extends RuntimeException {
    public SurveyMessageDecodingException() {
        super("주관식 설문 답변을 불러오는데 실패했습니다.");
    }
}
