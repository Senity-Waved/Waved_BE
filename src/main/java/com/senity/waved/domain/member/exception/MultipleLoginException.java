package com.senity.waved.domain.member.exception;

public class MultipleLoginException extends RuntimeException {
    public MultipleLoginException(String msg) {
        super(msg);
    }
}
