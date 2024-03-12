package com.senity.waved.domain.challengeGroup.repository;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeGroupRepository extends JpaRepository<ChallengeGroup, Long> {
    Optional<ChallengeGroup> findById(Long id);
}