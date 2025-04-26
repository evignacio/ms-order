package com.fiap.order.core.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String sku;
    private int amount;
    private BigDecimal value;

    public Product(String sku, int amount, BigDecimal value) {
        setSku(sku);
        setAmount(amount);
        setValue(value);
    }

    public String getSku() {
        return sku;
    }

    private void setSku(String sku) {
        if (sku == null || sku.isEmpty())
            throw new IllegalArgumentException("SKU cannot be null or empty");

        this.sku = sku;
    }

    public int getAmount() {
        return amount;
    }

    private void setAmount(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        this.amount = amount;
    }

    public BigDecimal getValue() {
        return value;
    }

    private void setValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Value cannot be null or negative");

        this.value = value;
    }

    public BigDecimal getTotalValue() {
        return value.multiply(BigDecimal.valueOf(amount));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sku);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product))
            return false;

        return this == o || sku.equals(product.sku);
    }

    @Override
    public String toString() {
        return "Product{" +
                "sku='" + sku + '\'' +
                ", amount=" + amount +
                ", value=" + value +
                '}';
    }
}
