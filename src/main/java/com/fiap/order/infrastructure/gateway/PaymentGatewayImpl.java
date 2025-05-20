package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.dto.CreditCardDTO;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.PaymentGateway;
import com.fiap.order.infrastructure.integration.rest.PaymentRestClient;
import com.fiap.order.infrastructure.integration.rest.to.CreatePaymentRequest;
import com.fiap.order.infrastructure.integration.rest.to.TokenCreditCard;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentGatewayImpl implements PaymentGateway {

    private final PaymentRestClient paymentRestClient;

    public PaymentGatewayImpl(PaymentRestClient paymentRestClient) {
        this.paymentRestClient = paymentRestClient;
    }

    @Override
    public void registerPaymentRequest(CreditCardDTO creditCardDTO, Order order) {
        try {
            log.info("Registering payment request for order {}", order.getId());
            var tokenCreditCard = new TokenCreditCard(creditCardDTO.token(), creditCardDTO.cvv());
            var request = new CreatePaymentRequest(tokenCreditCard, order.getId(), order.getTotalValue());
            paymentRestClient.create(request);
            log.info("Payment request registered successfully for order {}", order.getId());
        } catch (FeignException exception) {
            log.error("Error registering payment request for order {}", order.getId(), exception);
            throw new IllegalStateException("Error registering payment request");
        }
    }
}
