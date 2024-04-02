package com.senity.waved.domain.liked.repository;

import com.senity.waved.domain.liked.entity.Liked;
import com.senity.waved.domain.verification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    boolean existsByMemberIdAndVerification(Long memberId, Verification verification);
    Long countLikesByVerification(Verification verification);

    Optional<Liked> findByMemberIdAndVerification(Long memberId, Verification verification);
}
