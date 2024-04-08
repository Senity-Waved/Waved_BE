package com.senity.waved.domain.verification.repository;

import com.senity.waved.domain.verification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    List<Verification> findByChallengeGroupIdAndIsDeletedFalse(Long challengeGroupId);

    @Query("SELECT v FROM Verification v WHERE v.createDate >= :startOfDay AND v.createDate <= :endOfDay AND v.challengeGroupId = :challengeGroupId AND v.isDeleted = FALSE")
    List<Verification> findByCreateDateBetweenAndChallengeGroupAndIsDeletedFalse (
            @Param("startOfDay") ZonedDateTime startOfDay,
            @Param("endOfDay") ZonedDateTime endOfDay,
            @Param("challengeGroupId") Long challengeGroupId
    );

    List<Verification> findByMemberIdAndChallengeGroupIdAndCreateDateBetweenAndIsDeletedFalse(
            Long memberId, Long challengeGroupId, ZonedDateTime start, ZonedDateTime end);
}
