package com.fiap.order.core.usecase;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.exception.StockNotAvailableException;
import com.fiap.order.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

@Slf4j
@Service
public class ReserveStockUseCase {
    private final StockGateway stockGateway;

    public ReserveStockUseCase(StockGateway stockGateway) {
        this.stockGateway = stockGateway;
    }

    public void execute(Order order) {
        var itemsReserved = new LinkedHashSet<OrderItem>();
        order.getOrderItems().forEach(item -> {
            try {
                log.info("Reserving stock for product {}, amount {}", item.getSku(), item.getAmount());
                stockGateway.reserve(item.getSku(), item.getAmount());
                itemsReserved.add(item);
                order.defineAwaitingPayment();
            } catch (StockNotAvailableException exception) {
                log.error("Stock not available for product {}, amount {}", item.getSku(), item.getAmount(), exception);
                itemsReserved.forEach(i -> stockGateway.release(i.getSku(), i.getAmount()));
                order.defineNoStock();
            }
        });
    }
}
