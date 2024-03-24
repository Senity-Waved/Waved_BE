package com.senity.waved.domain.myChallenge.repository;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface MyChallengeRepository extends JpaRepository<MyChallenge, Long> {
    Optional<MyChallenge> findById(Long id);
    Optional<MyChallenge> findByMemberAndChallengeGroup(Member member, ChallengeGroup challengeGroup);

    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroup.id = cg.id WHERE mc.member.id = :memberId AND cg.startDate > :todayStart")
    List<MyChallenge> findMyChallengesWaiting(@Param("memberId") Long memberId, @Param("todayStart") ZonedDateTime todayStart);

    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroup.id = cg.id WHERE mc.member.id = :memberId AND cg.startDate < :todayStart AND cg.endDate >= :todayStart")
    List<MyChallenge> findMyChallengesInProgress(@Param("memberId") Long memberId, @Param("todayStart") ZonedDateTime todayStart);

    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroup.id = cg.id WHERE mc.member.id = :memberId AND cg.endDate < :todayStart")
    List<MyChallenge> findMyChallengesCompleted(@Param("memberId") Long memberId, @Param("todayStart") ZonedDateTime todayStart);

}

