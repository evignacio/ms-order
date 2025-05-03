package com.fiap.order.core.usecase;

import com.fiap.order.core.gateway.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteOrderUseCase {

    private final OrderGateway orderGateway;

    public DeleteOrderUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public void execute(String orderId) {
        log.info("Deleting order {}", orderId);
        orderGateway.find(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found, id: " + orderId));
        orderGateway.delete(orderId);
        log.info("Order {} deleted", orderId);
    }
}
