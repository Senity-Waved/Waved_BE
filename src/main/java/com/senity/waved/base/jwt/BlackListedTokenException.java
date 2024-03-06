package com.senity.waved.base.jwt;

public class BlackListedTokenException extends RuntimeException {
    public BlackListedTokenException(String msg) {
        super(msg);
    }
}
