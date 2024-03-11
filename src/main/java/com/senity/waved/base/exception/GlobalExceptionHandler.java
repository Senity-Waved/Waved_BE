package com.senity.waved.base.exception;

import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.member.exception.InvalidRefreshTokenException;
import com.senity.waved.domain.member.exception.WrongGithubInfoException;
import com.senity.waved.domain.myChallenge.exception.MemberNotFoundException;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.quiz.exception.QuizNotFoundException;
import com.senity.waved.domain.verification.exception.ChallengeGroupVerificationException;
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

    @ExceptionHandler(ChallengeGroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleChallengeGroupNotFoundException(ChallengeGroupNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(QuizNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleQuizNotFoundException(QuizNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ChallengeGroupVerificationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleChallengeGroupVerificationException(ChallengeGroupVerificationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MyChallengeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMyChallengeNotFoundException(MyChallengeNotFoundException e) {
        return e.getMessage();
    }
}
