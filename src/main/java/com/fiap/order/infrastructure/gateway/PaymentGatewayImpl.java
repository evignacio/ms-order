package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.dto.CreditCardDTO;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentGatewayImpl implements PaymentGateway {
    @Override
    public void registerPaymentRequest(CreditCardDTO creditCardDTO, Order order) {
        // Simulate registering a payment request
    }
}
