package com.senity.waved.domain.challengeGroup.repository;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeGroupRepository extends JpaRepository<ChallengeGroup, Long> {
    Optional<ChallengeGroup> getChallengeGroupByMyChallengesContains(MyChallenge myChallenge);

}