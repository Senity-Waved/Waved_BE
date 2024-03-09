package com.senity.waved.domain.member.exception;

public class WrongGithubInfoException extends RuntimeException {
    public WrongGithubInfoException(String msg) {
        super(msg);
    }
}