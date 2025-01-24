package com.collabit.oauth.exception;

public class GithubAlreadyLinkedException extends RuntimeException {
    public GithubAlreadyLinkedException(String message) {
        super(message);
    }
}