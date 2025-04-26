package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class OrderGatewayImpl implements OrderGateway {
    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Optional<Order> find(String orderId) {
        return Optional.empty();
    }

    @Override
    public Set<Order> findAll() {
        return Set.of();
    }

    @Override
    public void delete(String orderId) {
        // Simulate deleting the order from a database
        System.out.println("Order deleted: " + orderId);
    }
}
