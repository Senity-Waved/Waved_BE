package com.senity.waved.domain.paymentRecord.service;

import com.senity.waved.domain.paymentRecord.dto.request.PaymentRequestDto;

public interface PaymentRecordService {
    void validateAndSavePaymentRecord(String email, Long myChallengeId, PaymentRequestDto requestDto);
    String checkRefundDepositOrNot(String email, Long myChallengeId);
    void cancelChallengePayment(String email, Long myChallengeId);
}
