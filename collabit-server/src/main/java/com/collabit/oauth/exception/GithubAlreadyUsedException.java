package com.collabit.oauth.exception;

public class GithubAlreadyUsedException extends RuntimeException {
    public GithubAlreadyUsedException() {
        super("이 GitHub 계정은 이미 다른 계정과 연동되어 있습니다. 다른 GitHub 계정을 사용해 주세요.");
    }
}
