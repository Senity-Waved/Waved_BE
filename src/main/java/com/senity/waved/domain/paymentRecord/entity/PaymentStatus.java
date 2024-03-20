package com.senity.waved.domain.paymentRecord.entity;

public enum PaymentStatus {
    APPLIED, // 신청 ~ 종료 전까지의 상태 (ex. deposit=-5000)
    CANCELED, // 신청 취소해서 환불한 경우 (ex. deposit=5000)
    SUCCESS, // 챌린지 성공한 경우, 예치금 환급 (ex. deposit=5000)
    FAIL // 신청했지만 챌린지 실패한 경우, 예치금 미환급 (ex. deposit=0)
}