package com.senity.waved.domain.verification.repository;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.verification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    List<Verification> findByChallengeGroup(ChallengeGroup challengeGroup);

    @Query("SELECT v FROM Verification v WHERE v.createDate >= :startOfDay AND v.createDate <= :endOfDay AND v.challengeGroup = :challengeGroup AND v.isDeleted = FALSE")
    List<Verification> findByCreateDateBetweenAndChallengeGroupAndIsDeletedFalse (
            @Param("startOfDay") Timestamp startOfDay,
            @Param("endOfDay") Timestamp endOfDay,
            @Param("challengeGroup") ChallengeGroup challengeGroup);
}
