package com.fiap.order.infrastructure.integration.rest;

import com.fiap.order.infrastructure.integration.rest.to.UpdateStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "stockRestClient", url = "${rest-client.stock.url}")
public interface StockRestClient {

    @PutMapping("{sku}")
    ResponseEntity<Void> updateStock(@PathVariable String sku,  UpdateStockRequest request);

}
