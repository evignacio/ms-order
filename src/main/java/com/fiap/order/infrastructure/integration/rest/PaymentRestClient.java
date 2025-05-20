package com.fiap.order.infrastructure.integration.rest;

import com.fiap.order.infrastructure.integration.rest.to.CreatePaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentRestClient", url = "${rest-client.payment.url}")
public interface PaymentRestClient {

    @PostMapping
    ResponseEntity<Void> create(@RequestBody CreatePaymentRequest request);
}
