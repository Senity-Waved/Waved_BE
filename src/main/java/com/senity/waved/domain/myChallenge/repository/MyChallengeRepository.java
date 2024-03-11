package com.senity.waved.domain.myChallenge.repository;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyChallengeRepository extends JpaRepository<MyChallenge, Long> {
    Optional<MyChallenge> findById(Long id);
    Optional<MyChallenge> findByMemberAndChallengeGroup(Member member, ChallengeGroup challengeGroup);
//    Optional<Object> findByMemberAndChallengeGroup(Member member, ChallengeGroup challengeGroup);

/*    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroupId = cg.id WHERE mc.memberId = :memberId AND cg.startDate > :today")
    List<MyChallenge> findMyChallengesWaiting(@Param("memberId") Long memberId, @Param("today") Date today);

    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroupId = cg.id WHERE mc.memberId = :memberId AND cg.startDate < :today AND cg.endDate > :today")
    List<MyChallenge> findMyChallengesInProgress(@Param("memberId") Long memberId, @Param("today") Date today);

    @Query("SELECT mc FROM MyChallenge mc JOIN ChallengeGroup cg ON mc.challengeGroupId = cg.id WHERE mc.memberId = :memberId AND cg.endDate < :today")
    List<MyChallenge> findMyChallengesCompleted(@Param("memberId") Long memberId, @Param("today") Date today);*/
}
