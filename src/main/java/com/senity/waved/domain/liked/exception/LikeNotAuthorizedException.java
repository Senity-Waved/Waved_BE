package com.senity.waved.domain.liked.exception;

public class LikeNotAuthorizedException extends RuntimeException {
    public LikeNotAuthorizedException (String message) {
        super(message);
    }
}
