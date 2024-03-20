package com.senity.waved.domain.paymentRecord.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.member.entity.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long myChallengeId;

    public static PaymentRecord of(PaymentStatus status, Long deposit, Member member, Long myChallengeId) {
        return PaymentRecord.builder()
                .deposit(deposit)
                .paymentStatus(status)
                .member(member)
                .myChallengeId(myChallengeId)
                .build();
    }

}
