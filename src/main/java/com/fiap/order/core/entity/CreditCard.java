package com.fiap.order.core.entity;

import java.util.Objects;

public class CreditCard {
    private String number;
    private String name;
    private String expirationDate;
    private String cvv;

    public CreditCard(String number, String name, String expirationDate, String cvv) {
        setNumber(number);
        setName(name);
        setExpirationDate(expirationDate);
        setCvv(cvv);
    }

    private void setNumber(String number) {
        if (number == null || !number.matches("\\d{16}"))
            throw new IllegalArgumentException("Number must be 16 digits");

        this.number = number;
    }

    private void setName(String name) {
        if (name == null || name.length() < 3)
            throw new IllegalArgumentException("Name must be at least 3 characters long");

        this.name = name;
    }

    private void setExpirationDate(String expirationDate) {
        if (expirationDate == null || !expirationDate.matches("\\d{2}/\\d{2}"))
            throw new IllegalArgumentException("Expiration date must be in MM/YY format");

        this.expirationDate = expirationDate;
    }

    private void setCvv(String cvv) {
        if (cvv == null || !cvv.matches("\\d{3}"))
            throw new IllegalArgumentException("CVV must be 3 digits");

        this.cvv = cvv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCard that)) return false;
        return Objects.equals(number, that.number) && Objects.equals(expirationDate, that.expirationDate) && Objects.equals(cvv, that.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, expirationDate, cvv);
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", cvv=***" + '\'' +
                '}';
    }
}
