package com.senity.waved.domain.verification.repository;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.verification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    @Query("SELECT v FROM Verification v WHERE v.createDate >= :startOfDay AND v.createDate < :startOfNextDay AND v.challengeGroup = :challengeGroup")
    List<Verification> findByCreateDateBetweenAndChallengeGroup(@Param("startOfDay") Timestamp startOfDay, @Param("startOfNextDay") Timestamp startOfNextDay, @Param("challengeGroup") ChallengeGroup challengeGroup);
}
