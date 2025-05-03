package com.fiap.order.infrastructure.integration.rest;

import com.fiap.order.infrastructure.integration.rest.to.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product", url = "${rest-client..product.url}")
public interface ProductRestClient {

    @GetMapping("{sku}")
    ResponseEntity<ProductResponse> find(@PathVariable String sku);
}
