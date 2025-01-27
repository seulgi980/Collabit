package com.collabit.oauth.exception;

public class GithubAlreadyLinkedException extends RuntimeException {
    public GithubAlreadyLinkedException() {
        super("이 계정은 이미 GitHub와 연동되어 있습니다.");
    }
}