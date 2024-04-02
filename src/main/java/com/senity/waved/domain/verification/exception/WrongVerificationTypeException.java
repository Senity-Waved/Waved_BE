package com.senity.waved.domain.verification.exception;

public class WrongVerificationTypeException extends RuntimeException {
    public WrongVerificationTypeException(String message) {
        super(message);
    }
}
