package com.fiap.order.core.usecase;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class FindAllOrdersUseCase {

    private final OrderGateway orderGateway;

    public FindAllOrdersUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public Set<Order> execute() {
        log.info("Finding all orders");
        return orderGateway.findAll();
    }
}
