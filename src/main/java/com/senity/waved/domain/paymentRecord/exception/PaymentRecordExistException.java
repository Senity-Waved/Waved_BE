package com.senity.waved.domain.paymentRecord.exception;

public class PaymentRecordExistException extends RuntimeException {
    public PaymentRecordExistException(String msg) {
        super(msg);
    }
}
