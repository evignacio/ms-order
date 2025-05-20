package com.fiap.order.infrastructure.integration.rest.to;

public record TokenCreditCard(String token, String cvv) {
    @Override
    public String toString() {
        return "TokenCreditCard{" +
                "token='" + token + '\'' +
                ", cvv='***" + '\'' +
                '}';
    }
}
