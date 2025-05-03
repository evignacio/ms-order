package com.fiap.order.core.usecase;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FindOrderUseCase {

    private final OrderGateway orderGateway;

    public FindOrderUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public Order execute(String orderId) {
        log.info("Finding order {}", orderId);
        return orderGateway.find(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found, id: " + orderId));
    }
}
