package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockGatewayImpl implements StockGateway {
    @Override
    public void reserve(String sku, int amout) {
        // Simulate reserving stock
        System.out.println("Stock reserved: SKU=" + sku + ", Amount=" + amout);
    }

    @Override
    public void release(String sku, int amout) {
        // Simulate releasing stock
        System.out.println("Stock released: SKU=" + sku + ", Amount=" + amout);
    }
}
