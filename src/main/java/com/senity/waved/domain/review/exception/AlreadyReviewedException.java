package com.senity.waved.domain.review.exception;

public class AlreadyReviewedException extends RuntimeException {
    public AlreadyReviewedException(String message) {
        super(message);
    }
}
