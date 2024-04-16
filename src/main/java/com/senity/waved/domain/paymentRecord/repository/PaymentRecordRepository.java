package com.senity.waved.domain.paymentRecord.repository;

import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Page<PaymentRecord> getPaymentRecordByIdMemberIdWithin(Long memberId, Pageable pageable);

    // Optional<PaymentRecord> findByMemberIdAndMyChallengeIdAndPaymentStatus(Long memberId, Long myChallengeId, PaymentStatus paymentStatus);
}
