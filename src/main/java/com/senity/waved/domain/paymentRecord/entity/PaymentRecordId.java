package com.senity.waved.domain.paymentRecord.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRecordId implements Serializable {

    private Long memberId;
    private Long myChallengeId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Long getMemberId() {
        return memberId;
    }
}
