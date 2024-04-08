package com.senity.waved.domain.verification.exception;

public class AlreadyDeletedVerificationException extends RuntimeException {
    public AlreadyDeletedVerificationException(String message) {
        super(message);
    }
}
