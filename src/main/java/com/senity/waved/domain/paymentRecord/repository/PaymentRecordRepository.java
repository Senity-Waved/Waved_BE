package com.senity.waved.domain.paymentRecord.repository;

import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Page<PaymentRecord> getPaymentRecordByMemberId(Long memberId, Pageable pageable);

    Optional<PaymentRecord> findByMemberIdAndMyChallengeId(Long memberId, Long myChallengeId);
}
