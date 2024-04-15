package com.senity.waved.domain.paymentRecord.dto.response;

import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import com.senity.waved.domain.paymentRecord.entity.PaymentStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentRecordResponseDto {
    private String groupTitle;
    private PaymentStatus status;
    private Long deposit;
    private ZonedDateTime createDate;

    public static PaymentRecordResponseDto from(PaymentRecord paymentRecord) {
        return PaymentRecordResponseDto.builder()
                .groupTitle(paymentRecord.getGroupTitle())
                .status(paymentRecord.getPaymentStatus())
                .deposit(paymentRecord.getDeposit())
                .createDate(paymentRecord.getCreateDate())
                .build();
    }
}
