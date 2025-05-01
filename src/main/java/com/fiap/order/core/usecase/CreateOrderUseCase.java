package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.CreateOrderDTO;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.factory.OrderFactory;
import com.fiap.order.core.gateway.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateOrderUseCase {

    private final OrderGateway orderGateway;
    private final CustomerGateway customerGateway;
    private final CreateItemsOrderUseCase createItemsOrderUseCase;
    private final ReserveStockUseCase reserveStockUseCase;
    private final RegisterPaymentRequestUseCase registerPaymentRequestUseCase;

    public CreateOrderUseCase(OrderGateway orderGateway,
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

    public Order execute(CreateOrderDTO input) {
        var customer = input.customer();
        log.info("Creating order for customer {}", customer.costumerId());

        var address = customerGateway.findAddress(customer.costumerId(), customer.addressId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unable to find the customer address provided, customerId: %s, addressId: %s", customer.costumerId(), customer.addressId())));

        var orderItems = createItemsOrderUseCase.execute(input.orderItems());
        var order = OrderFactory.build(customer.costumerId(), orderItems, address);
        reserveStockUseCase.execute(order);

        if (order.isPaymentAvailable())
            registerPaymentRequestUseCase.execute(input.creditCard(), order);

        orderGateway.save(order);
        log.info("Order {} created", order.getId());
        return order;
    }
}
