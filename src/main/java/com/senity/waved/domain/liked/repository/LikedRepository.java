package com.senity.waved.domain.liked.repository;

import com.senity.waved.domain.liked.entity.Liked;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.verification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    boolean existsByMemberAndVerification(Member member, Verification verification);
}
