package com.fiap.order.core.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Order {
    private String id;
    private String customerId;
    private Set<Product> products;
    private Status status;
    private Instant createdAt;

    public Order(String id, String customerId, Set<Product> products, Status status, Instant createdAt) {
        setId(id);
        setCustomerId(customerId);
        setProducts(products);
        setStatus(status);
        setCreatedAt(createdAt);
    }

    public Order(String customerId, Set<Product> products) {
        this.id = UUID.randomUUID().toString();
        setCustomerId(customerId);
        setProducts(products);
        this.status = Status.PENDING;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("ID cannot be null or empty");

        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    private void setCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty())
            throw new IllegalArgumentException("Customer ID cannot be null or empty");

        this.customerId = customerId;
    }

    public Set<Product> getProducts() {
        return products;
    }

    private void setProducts(Set<Product> products) {
        if (products == null || products.isEmpty())
            throw new IllegalArgumentException("Products cannot be null or empty");

        this.products = products;
    }

    public void addProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");

        this.products.add(product);
    }

    public void removeProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");

        this.products.remove(product);
    }

    public Status getStatus() {
        return status;
    }

    private void setStatus(Status status) {
        if (status == null)
            throw new IllegalArgumentException("Status cannot be null");

        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(Instant createdAt) {
        if (createdAt == null)
            throw new IllegalArgumentException("Created At cannot be null");

        this.createdAt = createdAt;
    }

    public BigDecimal getTotalValue() {
        return products.stream()
                .map(Product::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void defineNoStock() {
        setStatus(Status.CLOSED_NO_STOCK);
    }

    public void definePaymentNotApproved() {
        setStatus(Status.CLOSED_PAYMENT_NOT_APPROVED);
    }

    public boolean isPending() {
        return status.equals(Status.PENDING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", products=" + products +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
