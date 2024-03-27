package com.senity.waved.domain.quiz.service;

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
    public Quiz getTodaysQuiz(Long challengeGroupId) {
        verificationService.challengeGroupIsTextType(challengeGroupId);

        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);
        return quizRepository.findByChallengeGroupIdAndDate(challengeGroupId, today)
                .orElseThrow(() -> new QuizNotFoundException("오늘의 퀴즈를 찾을 수 없습니다."));
    }

    //@Override
    public Quiz getQuizByDate1(Long challengeGroupId, ZonedDateTime quizDate) {
        verificationService.challengeGroupIsTextType(challengeGroupId);

        for (int i=1; i<=14; i++) {
            Quiz quiz = quizRepository.findById(i*1L)
                    .orElseThrow(() -> new RuntimeException("tmp"));
            log.error("-------------------------quiz id: "  + i + ", date: "+ quiz.getDate());
        }
//        ZonedDateTime requestedQuizDate = quizDate.truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime requestedQuizDate = quizDate.withZoneSameInstant(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);



        log.error("requestedQuizDate: " + requestedQuizDate);
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);
        log.error("today: " + today);

        return quizRepository.findQuizByChallengeGroupIdAndRequestDate(challengeGroupId, requestedQuizDate)
                .orElseThrow(() -> new QuizNotFoundException("해당 날짜의 퀴즈를 찾을 수 없습니다."));
    }

    @Override
    public Quiz getQuizByDate(Long challengeGroupId, ZonedDateTime quizDate) {
        verificationService.challengeGroupIsTextType(challengeGroupId);

        // 입력된 quizDate의 타임존을 'Asia/Seoul'로 변경하고, 날짜의 정밀도를 일(day) 단위로 조정
        ZonedDateTime requestedQuizDate = quizDate.withZoneSameInstant(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);

        log.error("requestedQuizDate: " + requestedQuizDate);
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.DAYS);
        log.error("today: " + today);

        return quizRepository.findQuizByChallengeGroupIdAndRequestDate(challengeGroupId, requestedQuizDate)
                .orElseThrow(() -> new QuizNotFoundException("해당 날짜의 퀴즈를 찾을 수 없습니다."));
    }

}

