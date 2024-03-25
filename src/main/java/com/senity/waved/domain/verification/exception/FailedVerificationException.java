package com.senity.waved.domain.verification.exception;

public class FailedVerificationException extends RuntimeException {
    public FailedVerificationException(String message) {
        super(message);
    }
}