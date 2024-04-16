package com.senity.waved.domain.paymentRecord.repository;

import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    @Query("SELECT p FROM PaymentRecord p WHERE p.id.memberId = :memberId")
    Page<PaymentRecord> getPaymentRecordByMemberId(@Param("memberId")Long memberId, Pageable pageable);
}
