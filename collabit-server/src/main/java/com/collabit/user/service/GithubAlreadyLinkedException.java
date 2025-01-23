package com.collabit.user.service;

public class GithubAlreadyLinkedException extends RuntimeException {
    public GithubAlreadyLinkedException(String message) {
        super(message);
    }
}