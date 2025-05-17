package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.CreditCardDTO;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.exception.PaymentNotApprovedException;
import com.fiap.order.core.gateway.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterPaymentRequestUseCase {

    private final PaymentGateway paymentGateway;
    private final ReleaseStockUseCase releaseStockUseCase;

    public RegisterPaymentRequestUseCase(PaymentGateway paymentGateway,
                                         ReleaseStockUseCase releaseStockUseCase) {
        this.paymentGateway = paymentGateway;
        this.releaseStockUseCase = releaseStockUseCase;
    }

    public void execute(CreditCardDTO creditCard, Order order) {
        log.info("Registering payment request for order {}", order.getId());
        if (!order.isPaymentAvailable())
            throw new IllegalStateException("Payment not available for order " + order.getId());

        try {
            log.info("Requesting payment for the order {}", order.getId());
            paymentGateway.registerPaymentRequest(creditCard, order);
            order.defineProcessingPayment();
        } catch (PaymentNotApprovedException exception) {
            log.error("Payment failed for order {}", order.getId(), exception);
            order.getOrderItems().forEach(p -> releaseStockUseCase.execute(order));
            order.definePaymentNotApproved();
        }
    }
}
