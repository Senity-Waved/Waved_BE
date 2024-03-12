package com.senity.waved.domain.review.repository;

import com.senity.waved.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long id);
}
