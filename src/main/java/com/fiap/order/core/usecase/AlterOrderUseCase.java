package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.AlterOrderDTO;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.factory.OrderFactory;
import com.fiap.order.core.gateway.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlterOrderUseCase {

    private final OrderGateway orderGateway;
    private final CustomerGateway customerGateway;
    private final CreateItemsOrderUseCase createItemsOrderUseCase;
    private final ReserveStockUseCase reserveStockUseCase;
    private final RegisterPaymentRequestUseCase registerPaymentRequestUseCase;

    public AlterOrderUseCase(OrderGateway orderGateway,
                             CustomerGateway customerGateway,
                             CreateItemsOrderUseCase createItemsOrderUseCase,
                             ReserveStockUseCase reserveStockUseCase,
                             RegisterPaymentRequestUseCase registerPaymentRequestUseCase) {
        this.orderGateway = orderGateway;
        this.customerGateway = customerGateway;
        this.createItemsOrderUseCase = createItemsOrderUseCase;
        this.reserveStockUseCase = reserveStockUseCase;
        this.registerPaymentRequestUseCase = registerPaymentRequestUseCase;
    }

    public Order execute(AlterOrderDTO input) {
        log.info("Changing order id: {}", input.orderId());

        var order = orderGateway.find(input.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found, id:" + input.orderId()));

        if (order.isClosed())
            throw new IllegalStateException("Order status does not allow changes");

        var address = customerGateway.findAddress(order.getCustomerId(), input.addressId())
                .orElseThrow(() -> new IllegalArgumentException("Unable to find the customer address provided, customerId: " + order.getCustomerId() + ", addressId: " + input.addressId()));

        var orderItems = createItemsOrderUseCase.execute(input.orderItems());
        var newOrder = OrderFactory.build(order.getCustomerId(), orderItems, address);
        reserveStockUseCase.execute(newOrder);

        if (newOrder.isPaymentAvailable())
            registerPaymentRequestUseCase.execute(input.creditCard(), newOrder);

        orderGateway.save(order);
        log.info("Order {} changed", order.getId());
        return order;
    }
}
