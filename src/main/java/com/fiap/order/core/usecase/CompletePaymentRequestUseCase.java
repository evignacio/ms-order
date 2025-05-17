package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.PaymentStatus;
import com.fiap.order.core.gateway.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompletePaymentRequestUseCase {

    private final ReleaseStockUseCase releaseStockUseCase;
    private final FindOrderUseCase findOrderUseCase;
    private final OrderGateway orderGateway;

    public CompletePaymentRequestUseCase(ReleaseStockUseCase releaseStockUseCase,
                                         FindOrderUseCase findOrderUseCase,
                                         OrderGateway orderGateway) {
        this.releaseStockUseCase = releaseStockUseCase;
        this.findOrderUseCase = findOrderUseCase;
        this.orderGateway = orderGateway;
    }

    public void execute(String orderId, PaymentStatus paymentStatus) {
        log.info("Completing payment request for order {}", orderId);

        var order = findOrderUseCase.execute(orderId);
        if (PaymentStatus.APPROVED.equals(paymentStatus)) {
            order.defineCompleted();
        } else {
            releaseStockUseCase.execute(order);
            order.definePaymentNotApproved();
        }
        orderGateway.save(order);
        log.info("Payment request completed for order {}", orderId);
    }
}
