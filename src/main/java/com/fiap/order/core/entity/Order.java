package com.fiap.order.core.entity;

import com.fiap.order.core.entity.valueobject.Address;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Order {
    private String id;
    private String customerId;
    private Set<OrderItem> orderItems;
    private Address address;
    private Status status;
    private Instant createdAt;

    public Order(String id, String customerId, Set<OrderItem> orderItems, Address address, Status status, Instant createdAt) {
        setId(id);
        setCustomerId(customerId);
        setOrderItems(orderItems);
        setAddress(address);
        setStatus(status);
        setCreatedAt(createdAt);
    }

    public Order(String customerId, Set<OrderItem> orderItems, Address address) {
        this.id = UUID.randomUUID().toString();
        setCustomerId(customerId);
        setOrderItems(orderItems);
        setAddress(address);
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

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    private void setOrderItems(Set<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty())
            throw new IllegalArgumentException("Products cannot be null or empty");

        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem == null)
            throw new IllegalArgumentException("OrderItemDTO cannot be null");

        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (orderItem == null)
            throw new IllegalArgumentException("OrderItemDTO cannot be null");

        this.orderItems.remove(orderItem);
    }

    public Address getAddress() {
        return address;
    }

    private void setAddress(Address address) {
        if (address == null)
            throw new IllegalArgumentException("Address cannot be null");

        this.address = address;
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
        return orderItems.stream()
                .map(OrderItem::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void defineNoStock() {
        setStatus(Status.CLOSED_NO_STOCK);
    }

    public void definePaymentNotApproved() {
        setStatus(Status.CLOSED_PAYMENT_NOT_APPROVED);
    }

    public boolean isPaymentAvailable() {
        return status.equals(Status.PENDING) || status.equals(Status.CLOSED_PAYMENT_NOT_APPROVED);
        //O Status CLOSED_PAYMENT_NOT_APPROVED é o status que indica que o pagamento não foi aprovado, mas ainda pode ter outras tentativas de pagamento.
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
                ", orderItems=" + orderItems +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
