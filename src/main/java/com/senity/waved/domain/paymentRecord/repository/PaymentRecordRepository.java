package com.senity.waved.domain.paymentRecord.repository;

import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
}
