package com.fiap.order.core.gateway;

import com.fiap.order.core.dto.CreditCardDTO;
import com.fiap.order.core.entity.CreditCard;
import com.fiap.order.core.entity.Order;

public interface PaymentGateway {
    void registerPaymentRequest(CreditCard creditCard, Order order);
}
