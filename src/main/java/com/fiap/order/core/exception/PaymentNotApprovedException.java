package com.fiap.order.core.exception;

public class PaymentNotApprovedException extends RuntimeException {

    public PaymentNotApprovedException(String message) {
        super(message);
    }
}
