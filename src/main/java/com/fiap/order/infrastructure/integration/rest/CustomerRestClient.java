package com.fiap.order.infrastructure.integration.rest;

import com.fiap.order.infrastructure.integration.rest.to.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer", url = "${rest-client.customer.url}")
public interface CustomerRestClient {

    @GetMapping("{id}")
    ResponseEntity<CustomerResponse> find(@PathVariable String id);
}
