package com.senity.waved.base.exception;

import com.senity.waved.common.ResponseDto;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotCompletedException;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.liked.exception.DuplicationLikeException;
import com.senity.waved.domain.liked.exception.LikeNotAuthorizedException;
import com.senity.waved.domain.member.exception.InvalidRefreshTokenException;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.exception.WrongGithubInfoException;
import com.senity.waved.domain.myChallenge.exception.AlreadyMyChallengeExistsException;
import com.senity.waved.domain.myChallenge.exception.InvalidChallengeStatusException;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.paymentRecord.exception.DepositAmountNotMatchException;
import com.senity.waved.domain.paymentRecord.exception.MemberAndMyChallengeNotMatchException;
import com.senity.waved.domain.paymentRecord.exception.PaymentRecordExistException;
import com.senity.waved.domain.quiz.exception.QuizNotFoundException;
import com.senity.waved.domain.review.exception.AlreadyReviewedException;
import com.senity.waved.domain.review.exception.ReviewNotFoundException;
import com.senity.waved.domain.review.exception.UnauthorizedReviewAccessException;
import com.senity.waved.domain.verification.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(WrongGithubInfoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleWrongGithubInfoException(WrongGithubInfoException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleInvalidRefreshTokenException(InvalidRefreshTokenException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MemberAndMyChallengeNotMatchException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleMemberAndMyChallengeNotMatch(MemberAndMyChallengeNotMatchException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }


    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleReviewNotFoundException(ReviewNotFoundException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AlreadyReviewedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleAlreadyReviewedException(AlreadyReviewedException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedReviewAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleUnauthorizedReviewAccessException(UnauthorizedReviewAccessException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }


    @ExceptionHandler(ChallengeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleChallengeNotFoundException(ChallengeNotFoundException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(MyChallengeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleMyChallengeNotFoundException(MyChallengeNotFoundException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AlreadyMyChallengeExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleAlreadyMyChallengeExistsException(AlreadyMyChallengeExistsException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(InvalidChallengeStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleInvalidChallengeStatusException(InvalidChallengeStatusException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    @ExceptionHandler(ChallengeGroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleChallengeGroupNotFoundException(ChallengeGroupNotFoundException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ChallengeGroupNotCompletedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleChallengeGroupNotCompletedException(ChallengeGroupNotCompletedException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(QuizNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleQuizNotFoundException(QuizNotFoundException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(VerificationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleVerificationNotFoundException(VerificationNotFoundException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(VerificationNotTextException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleVerificationNotTextException(VerificationNotTextException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AlreadyVerifiedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleAlreadyVerifiedException(AlreadyVerifiedException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(AlreadyDeletedVerificationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleAlreadyDeletedVerificationException(AlreadyDeletedVerificationException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(VerifyNotFoundOnDateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleVerifyNotFoundOnDateException(VerifyNotFoundOnDateException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(WrongVerificationTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleWrongVerificationTypeException(WrongVerificationTypeException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NoVerificationFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleNoVerificationFieldException(NoVerificationFieldException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UnsupportedAuthenticationTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleUnsupportedAuthenticationTypeException(UnsupportedAuthenticationTypeException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(GitHubConnectionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto> handleGitHubConnectionException(GitHubConnectionException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }



    @ExceptionHandler(DuplicationLikeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleDuplicationLikeException(DuplicationLikeException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(LikeNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseDto> handleLikeNotAuthorizedException(LikeNotAuthorizedException e) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(DepositAmountNotMatchException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleDepositAmountNotMatchException(DepositAmountNotMatchException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(FailedVerificationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handleFailedVerificationException(FailedVerificationException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(PaymentRecordExistException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseDto> handlePaymentRecordExistException(PaymentRecordExistException e) {
        return ResponseDto.of(HttpStatus.FORBIDDEN, e.getMessage());
    }
}
