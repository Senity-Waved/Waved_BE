package com.senity.waved.domain.myChallenge.repository;

import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MyChallengeRepository extends JpaRepository<MyChallenge, Long> {
    Optional<MyChallenge> findById(Long id);

/*    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroupId = cg.id WHERE mc.memberId = :memberId AND cg.startDate > :today")
    List<MyChallenge> findMyChallengesWaiting(@Param("memberId") Long memberId, @Param("today") Date today);

    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroupId = cg.id WHERE mc.memberId = :memberId AND cg.startDate < :today AND cg.endDate > :today")
    List<MyChallenge> findMyChallengesInProgress(@Param("memberId") Long memberId, @Param("today") Date today);

    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroupId = cg.id WHERE mc.memberId = :memberId AND cg.endDate < :today")
    List<MyChallenge> findMyChallengesCompleted(@Param("memberId") Long memberId, @Param("today") Date today);*/
}
