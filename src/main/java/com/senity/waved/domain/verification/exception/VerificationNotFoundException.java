package com.senity.waved.domain.verification.exception;

public class VerificationNotFoundException extends RuntimeException {
    public VerificationNotFoundException (String message) {
        super(message);
    }
}
