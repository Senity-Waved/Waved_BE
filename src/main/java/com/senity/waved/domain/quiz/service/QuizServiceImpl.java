package com.senity.waved.domain.quiz.service;

import com.senity.waved.domain.quiz.entity.Quiz;
import com.senity.waved.domain.quiz.exception.QuizNotFoundException;
import com.senity.waved.domain.quiz.repository.QuizRepository;
import com.senity.waved.domain.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final VerificationService verificationService;

    @Override
    public Quiz getTodaysQuiz(Long challengeGroupId) {
        verificationService.challengeGroupIsTextType(challengeGroupId);

        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);
        return quizRepository.findByChallengeGroupIdAndDate(challengeGroupId, today)
                .orElseThrow(() -> new QuizNotFoundException("오늘의 퀴즈를 찾을 수 없습니다."));
    }

    @Override
    public Quiz getQuizByDate(Long challengeGroupId, ZonedDateTime quizDate) {
        verificationService.challengeGroupIsTextType(challengeGroupId);

        for (int i=1; i<=14; i++) {
            Quiz quiz = quizRepository.findById(i*1L)
                    .orElseThrow(() -> new RuntimeException("tmp"));
            System.out.println("-------------------------quiz id: "  + i + ", date: "+ quiz.getDate());
        }
        ZonedDateTime requestedQuizDate = quizDate.truncatedTo(ChronoUnit.DAYS);

        System.out.println("-------------------------------");
        System.out.println("requestedQuizDate: " + requestedQuizDate);
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);
        System.out.println("today: " + today);
        System.out.println("-------------------------------");

        return quizRepository.findQuizByChallengeGroupIdAndRequestDate(challengeGroupId, requestedQuizDate)
                .orElseThrow(() -> new QuizNotFoundException("해당 날짜의 퀴즈를 찾을 수 없습니다."));
    }
}

