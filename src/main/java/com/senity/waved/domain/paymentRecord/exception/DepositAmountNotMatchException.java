package com.senity.waved.domain.paymentRecord.exception;

public class DepositAmountNotMatchException extends RuntimeException {
    public DepositAmountNotMatchException(String msg) {
        super(msg);
    }
}
