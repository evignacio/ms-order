package com.fiap.order.core.dto;

public record CreditCardDTO(String token, String cvv) {
    @Override
    public String toString() {
        return "CreditCardDTO{" +
                "token='" + token + '\'' +
                ", cvv=***" + '\'' +
                '}';
    }
}
