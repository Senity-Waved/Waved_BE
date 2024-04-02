package com.senity.waved.domain.paymentRecord.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    public static PaymentRecord of(PaymentStatus status, Long memberId, MyChallenge myChallenge, String groupTitle) {
        Long deposit = status.equals(PaymentStatus.APPLIED) ?
                myChallenge.getDeposit() * (-1) : status.equals(PaymentStatus.FAIL) ? 0 : myChallenge.getDeposit();

        return PaymentRecord.builder()
                .deposit(deposit)
                .paymentStatus(status)
                .memberId(memberId)
                .myChallengeId(myChallenge.getId())
                .groupTitle(groupTitle)
                .build();
    }
}
