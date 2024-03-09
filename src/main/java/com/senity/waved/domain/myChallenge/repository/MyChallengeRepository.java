package com.senity.waved.domain.myChallenge.repository;

import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyChallengeRepository extends JpaRepository<MyChallenge, Long> {
    Optional<MyChallenge> findMyChallengeById(Long id);
}
