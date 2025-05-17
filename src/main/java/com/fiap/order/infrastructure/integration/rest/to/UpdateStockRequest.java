package com.fiap.order.infrastructure.integration.rest.to;

public record UpdateStockRequest(StockOperation stockOperation, Integer quantity) {
}
