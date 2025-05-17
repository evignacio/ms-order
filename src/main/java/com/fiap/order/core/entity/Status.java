package com.fiap.order.core.entity;

public enum Status {
    PENDING,
    AWAITING_PAYMENT,
    PROCESSING_PAYMENT,
    PAYMENT_NOT_APPROVED,
    NO_STOCK,
    CANCELED,
    COMPLETED,
}
