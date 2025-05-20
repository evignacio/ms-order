package com.fiap.order.core.gateway;

public interface StockGateway {
    void reserve(String sku, int amout);

    void release(String sku, int amout);
}
