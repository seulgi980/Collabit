package com.collabit.community.exception;

public class ParentCommentNotFoundException extends RuntimeException {
    public ParentCommentNotFoundException() {
        super("부모 댓글을 찾을 수 없습니다.");
    }
}
