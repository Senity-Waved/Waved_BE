package com.senity.waved.domain.quiz.service;

import com.senity.waved.domain.quiz.entity.Quiz;

import java.time.ZonedDateTime;

public interface QuizService {
    Quiz getTodaysQuiz(Long challengeGroupId);

    Quiz getQuizByDate(Long challengeGroupId, ZonedDateTime quizDate);
}
