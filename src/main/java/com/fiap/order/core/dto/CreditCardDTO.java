package com.fiap.order.core.dto;

public record CreditCardDTO(String number, String name, String expirationDate, String cvv) {
    @Override
    public String toString() {
        return "CreditCardDTO{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", cvv=***" + '\'' +
                '}';
    }
}
