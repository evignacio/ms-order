package com.fiap.order.core.usecase;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReleaseStockUseCase {

    private final StockGateway stockGateway;

    public ReleaseStockUseCase(StockGateway stockGateway) {
        this.stockGateway = stockGateway;
    }

    public void execute(Order order) {
        log.info("Releasing stock for order {}", order.getId());
        order.getOrderItems()
                .forEach(item -> {
                    log.info("Releasing stock for product {}, amount {}", item.getSku(), item.getAmount());
                    stockGateway.release(item.getSku(), item.getAmount());
                });
        log.info("Stock released for order {}", order.getId());
    }
}
