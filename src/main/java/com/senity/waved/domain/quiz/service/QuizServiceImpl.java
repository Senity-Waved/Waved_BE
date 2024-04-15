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

        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Z")).truncatedTo(ChronoUnit.DAYS);

        //Quiz quiz = findQuizByDate(challengeGroupId, today);
        Quiz quiz = quizRepository.findById(9L).orElseThrow(() -> new QuizNotFoundException("웅앵웅"));
        ZonedDateTime plusDate = quiz.getDate();


        log.info("++++++++++++++++++ todayQuiz&Zoned : " + today.truncatedTo(ChronoUnit.DAYS));
        log.info("++++++++++++++++++ todayQuiz&local : " + today.toLocalDate());


        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.error("----------------------------------------------------------------------");
        log.info("_____________________ todayQuiz : " + today);
        log.info("_____________________ Quizdate  : " + plusDate);
        log.info("______________ todayQuiz zoneId : " + today.getZone());
        log.info("______________ Quizdate zoneId  : " + plusDate.getZone());

        ZonedDateTime today2 = ZonedDateTime.now(plusDate.getZone()).truncatedTo(ChronoUnit.DAYS);
        Quiz quiz2 = findQuizByDate(challengeGroupId, today2);
        if(today2.equals(quiz2.getDate())) {
            log.error("--------------------------- today.equals(quiz.getDate()) SUCCESS!!");
        }


        return new QuizResponseDto(plusDate, quiz.getQuestion());
    }

    @Override
    public QuizResponseDto getQuizByDate(Long challengeGroupId, Timestamp requestedQuizDate) {

        ZonedDateTime quizDate = requestedQuizDate.toInstant().atZone(ZoneId.of("Z"))
                .withZoneSameInstant(ZoneId.of("Z")).truncatedTo(ChronoUnit.DAYS);

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

