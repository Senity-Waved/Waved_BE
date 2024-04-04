package com.senity.waved.domain.challengeGroup.repository;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface ChallengeGroupRepository extends JpaRepository<ChallengeGroup, Long> {
    @Query("SELECT cg FROM ChallengeGroup cg WHERE cg.startDate <= :todayStart AND cg.endDate >= :todayStart")
    List<ChallengeGroup> findChallengeGroupsInProgress(@Param("todayStart") ZonedDateTime todayStart);

    List<ChallengeGroup> findByChallengeIdAndGroupIndex(Long challengeId, Long groupIndex);
}