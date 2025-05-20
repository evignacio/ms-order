package com.fiap.order.core.gateway;

import com.fiap.order.core.entity.Order;

import java.util.Optional;
import java.util.Set;

public interface OrderGateway {
    Order save(Order order);

    Optional<Order> find(String orderId);

    Set<Order> findAll();

    void delete(String orderId);
}
