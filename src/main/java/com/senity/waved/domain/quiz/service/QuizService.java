package com.senity.waved.domain.quiz.service;

import com.senity.waved.domain.quiz.entity.Quiz;

public interface QuizService {
    Quiz findValidQuizByChallengeGroupId(Long challengeGroupId);
}
