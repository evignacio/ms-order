package com.fiap.order.core.gateway;

import com.fiap.order.core.entity.Order;

public interface OrderGateway {
    void save(Order order);

    void find(String orderId);

    void findAll();

    void delete(String orderId);
}
