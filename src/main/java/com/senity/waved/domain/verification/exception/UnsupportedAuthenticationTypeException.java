package com.senity.waved.domain.verification.exception;

public class UnsupportedAuthenticationTypeException extends RuntimeException {
    public UnsupportedAuthenticationTypeException(String message) {
        super(message);
    }
}
