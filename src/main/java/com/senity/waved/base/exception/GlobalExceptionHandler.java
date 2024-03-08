package com.senity.waved.base.exception;

import com.senity.waved.domain.member.exception.InvalidRefreshTokenException;
import com.senity.waved.domain.member.exception.WrongGithubInfoException;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMemberNotFoundException(MemberNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(WrongGithubInfoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWrongGithubInfoException(WrongGithubInfoException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidRefreshTokenException(InvalidRefreshTokenException e) {
        return e.getMessage();
    }
}
