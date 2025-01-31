package com.collabit.community.exception;

public class ImageCountExceededException extends RuntimeException {

    public ImageCountExceededException() {
        super("이미지 업로드 개수 제한을 초과했습니다.");
    }
}
