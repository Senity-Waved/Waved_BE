package com.senity.waved.domain.liked.exception;

public class DuplicationLikeException extends RuntimeException {
    public DuplicationLikeException(String message) {
        super(message);
    }
}