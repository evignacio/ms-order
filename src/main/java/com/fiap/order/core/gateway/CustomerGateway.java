package com.fiap.order.core.gateway;

public interface CustomerGateway {
    boolean exists(String customerId);
}
