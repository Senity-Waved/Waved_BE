package com.senity.waved.domain.paymentRecord.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.paymentRecord.dto.response.PaymentRecordResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PaymentRecord extends BaseEntity {

    @Column(name="payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "group_title")
    private String groupTitle;

    @Column(name = "my_challenge_id")
    private Long myChallengeId;

    public static PaymentRecord of(PaymentStatus status, Long deposit, Long memberId, Long myChallengeId, String groupTitle) {
        return PaymentRecord.builder()
                .deposit(deposit)
                .paymentStatus(status)
                .memberId(memberId)
                .myChallengeId(myChallengeId)
                .groupTitle(groupTitle)
                .build();
    }

    public static PaymentRecordResponseDto getPaymentResponse(PaymentRecord paymentRecord) {
        return PaymentRecordResponseDto.builder()
                .groupTitle(paymentRecord.getGroupTitle())
                .status(paymentRecord.getPaymentStatus())
                .deposit(paymentRecord.getDeposit())
                .createDate(paymentRecord.getCreateDate())
                .build();
    }
}
