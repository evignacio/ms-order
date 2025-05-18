package com.fiap.order.infrastructure.integration.rest.to;

import java.math.BigDecimal;

public record CreatePaymentRequest(TokenCreditCard tokenCreditCard, String orderId, BigDecimal amount) {
    @Override
    public String toString() {
        return "CreatePaymentRequest{" +
                "tokenCreditCard=" + tokenCreditCard +
                ", orderId='" + orderId + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
