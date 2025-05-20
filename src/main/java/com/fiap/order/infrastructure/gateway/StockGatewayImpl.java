package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.gateway.StockGateway;
import com.fiap.order.infrastructure.integration.rest.StockRestClient;
import com.fiap.order.infrastructure.integration.rest.to.StockOperation;
import com.fiap.order.infrastructure.integration.rest.to.UpdateStockRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockGatewayImpl implements StockGateway {

    private final StockRestClient stockRestClient;

    public StockGatewayImpl(StockRestClient stockRestClient) {
        this.stockRestClient = stockRestClient;
    }

    @Override
    public void reserve(String sku, int amout) {
        log.info("Reserving product from stock, sku: {}, amout: {}", sku, amout);
        stockRestClient.updateStock(sku, new UpdateStockRequest(StockOperation.DECREASE, amout));
        log.info("Product successfully reserved in stock, sku {}, amout: {}", sku, amout);
    }

    @Override
    public void release(String sku, int amout) {
        log.info("Releasing product from stock, sku: {}, amout: {}", sku, amout);
        stockRestClient.updateStock(sku, new UpdateStockRequest(StockOperation.INCREASE, amout));
        log.info("Product successfully released in stock, sku {}, amout: {}", sku, amout);
    }
}
