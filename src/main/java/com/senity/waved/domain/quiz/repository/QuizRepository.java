package com.senity.waved.domain.quiz.repository;

import com.senity.waved.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByChallengeGroupIdAndDate(Long challengeGroupId, ZonedDateTime today);

    @Query("SELECT q FROM Quiz q WHERE q.challengeGroup.id = :challengeGroupId AND q.date = :quizDate")
    Optional<Quiz> findQuizByChallengeGroupIdAndRequestDate(@Param("challengeGroupId") Long challengeGroupId,
                                                           @Param("quizDate") ZonedDateTime quizDate);
}