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

import java.sql.Timestamp;
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
        verificationService.IsChallengeGroupTextType(challengeGroupId);

        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.DAYS);

        Quiz quiz = findQuizByDate(challengeGroupId, today);

        ZonedDateTime plusDate = quiz.getDate();
        log.info("_____________________ todayQuiz : " + today);
        log.info("_____________________ Quizdate  : " + plusDate);
        return new QuizResponseDto(plusDate, quiz.getQuestion());
    }

    @Override
    public QuizResponseDto getQuizByDate(Long challengeGroupId, Timestamp requestedQuizDate) {
        ZonedDateTime quizDate = requestedQuizDate.toInstant().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        verificationService.IsChallengeGroupTextType(challengeGroupId);
        Quiz quiz = findQuizByDate(challengeGroupId, quizDate);

        ZonedDateTime plusDate = quiz.getDate();
        return new QuizResponseDto(plusDate, quiz.getQuestion());
    }

    private Quiz findQuizByDate(Long challengeGroupId, ZonedDateTime date) {
        return quizRepository.findByChallengeGroupIdAndDate(challengeGroupId, date)
                .orElseThrow(() -> new QuizNotFoundException("퀴즈를 찾을 수 없습니다."));
    }
}

