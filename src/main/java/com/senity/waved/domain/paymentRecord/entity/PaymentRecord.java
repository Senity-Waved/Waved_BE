package com.senity.waved.domain.paymentRecord.entity;

import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PaymentRecord {

    @EmbeddedId
    private PaymentRecordId id;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private ZonedDateTime createDate;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "group_title")
    private String groupTitle;

/*
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "my_challenge_id")
    private Long myChallengeId;

    @Column(name="payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
*/

    public static PaymentRecord of(PaymentStatus status, Long memberId, MyChallenge myChallenge, String groupTitle) {
        Long deposit = status.equals(PaymentStatus.APPLIED) ?
                myChallenge.getDeposit() * (-1) : status.equals(PaymentStatus.FAIL) ? 0 : myChallenge.getDeposit();
        PaymentRecordId paymentId = new PaymentRecordId(memberId, myChallenge.getId(), status);

        return PaymentRecord.builder()
                .deposit(deposit)
                .id(paymentId)
                .groupTitle(groupTitle)
                .build();
    }
}

