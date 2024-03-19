package com.senity.waved.domain.quiz.repository;

import com.senity.waved.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByChallengeGroupIdAndDate(Long challengeGroupId, ZonedDateTime today);
}