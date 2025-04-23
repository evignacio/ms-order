package com.fiap.order.core.entity;

import java.math.BigDecimal;

public class Product {
    private String sku;
    private int amount;
    private BigDecimal price;

    public Product(String sku, int amount, BigDecimal price) {
        setSku(sku);
        setAmount(amount);
        setPrice(price);
    }

    private void setSku(String sku) {
        if (sku == null || sku.isEmpty())
            throw new IllegalArgumentException("SKU cannot be null or empty");

        this.sku = sku;
    }

    private void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Price cannot be null or negative");

        this.price = price;
    }

    private void setAmount(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        this.amount = amount;
    }
}
