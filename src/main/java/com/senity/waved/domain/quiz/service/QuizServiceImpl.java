package com.senity.waved.domain.quiz.service;

import com.senity.waved.domain.quiz.dto.response.QuizResponseDto;
import com.senity.waved.domain.quiz.entity.Quiz;
import com.senity.waved.domain.quiz.exception.QuizNotFoundException;
import com.senity.waved.domain.quiz.repository.QuizRepository;
import com.senity.waved.domain.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final VerificationService verificationService;

    @Override
    public QuizResponseDto getTodaysQuiz(Long challengeGroupId) {
        verificationService.challengeGroupIsTextType(challengeGroupId);

        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);
        Quiz quiz = quizRepository.findByChallengeGroupIdAndDate(challengeGroupId, today)
                .orElseThrow(() -> new QuizNotFoundException("오늘의 퀴즈를 찾을 수 없습니다."));
        ZonedDateTime plusDate = quiz.getDate().plusHours(9);
        return new QuizResponseDto(plusDate, quiz.getQuestion());
    }

    @Override
    public QuizResponseDto getQuizByDate(Long challengeGroupId, ZonedDateTime quizDate) {
        verificationService.challengeGroupIsTextType(challengeGroupId);

        ZonedDateTime requestedQuizDate = quizDate.withZoneSameInstant(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);
        Quiz quiz = quizRepository.findQuizByChallengeGroupIdAndRequestDate(challengeGroupId, requestedQuizDate)
                .orElseThrow(() -> new QuizNotFoundException("해당 날짜의 퀴즈를 찾을 수 없습니다."));
        ZonedDateTime plusDate = quiz.getDate().plusHours(9);
        return new QuizResponseDto(plusDate, quiz.getQuestion());
    }
}

