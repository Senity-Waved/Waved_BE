package com.senity.waved.domain.quiz.service;

import com.senity.waved.domain.quiz.dto.response.QuizResponseDto;

import java.time.ZonedDateTime;

public interface QuizService {
    QuizResponseDto getTodaysQuiz(Long challengeGroupId);
    QuizResponseDto getQuizByDate(Long challengeGroupId, ZonedDateTime quizDate);
}
