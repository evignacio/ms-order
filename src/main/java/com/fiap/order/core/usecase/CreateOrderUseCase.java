package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.CreateOrderDTO;
import com.fiap.order.core.dto.CreditCardDTO;
import com.fiap.order.core.dto.OrderItemDTO;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.exception.PaymentNotApprovedException;
import com.fiap.order.core.exception.StockNotAvailableException;
import com.fiap.order.core.gateway.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
public class CreateOrderUseCase {

    private final OrderGateway orderGateway;
    private final CustomerGateway customerGateway;
    private final ProductGateway productGateway;
    private final PaymentGateway paymentGateway;
    private final StockGateway stockGateway;

    public CreateOrderUseCase(OrderGateway orderGateway,
                              CustomerGateway customerGateway,
                              ProductGateway productGateway,
                              PaymentGateway paymentGateway,
                              StockGateway stockGateway) {
        this.orderGateway = orderGateway;
        this.customerGateway = customerGateway;
        this.productGateway = productGateway;
        this.paymentGateway = paymentGateway;
        this.stockGateway = stockGateway;
    }

    public Order execute(CreateOrderDTO input) {
        var customer = input.customer();
        log.info("Creating order for customer {}", customer.costumerId());

        var address = customerGateway.findAddress(customer.costumerId(), customer.addressId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unable to find the customer address provided, customerId: %s, addressId: %s", customer.costumerId(), customer.addressId())));

        var orderItems = new HashSet<OrderItem>();
        for (OrderItemDTO item : input.orderItems()) {
            var product = productGateway.find(item.sku())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found, sku:" + item.sku()));

            var orderItem = new OrderItem(item.sku(), item.amount(), product.value());
            orderItems.add(orderItem);
        }
        var order = new Order(customer.costumerId(), orderItems, address);
        reserveStock(order);

        if (order.isPaymentAvailable())
            registerPaymentRequest(input.creditCard(), order);

        orderGateway.save(order);
        log.info("Order {} created", order.getId());
        return order;
    }

    private void reserveStock(Order order) {
        order.getOrderItems().forEach(p -> {
            try {
                log.info("Reserving stock for product {}, amount {}", p.getSku(), p.getAmount());
                stockGateway.reserve(p.getSku(), p.getAmount());
            } catch (StockNotAvailableException exception) {
                log.error("Stock not available for product {}, amount {}", p.getSku(), p.getAmount(), exception);
                order.defineNoStock();
            }
        });
    }

    private void registerPaymentRequest(CreditCardDTO creditCard, Order order) {
        try {
            log.info("Requesting payment for the order {}", order.getId());
            paymentGateway.registerPaymentRequest(creditCard, order);
        } catch (PaymentNotApprovedException exception) {
            log.error("Payment failed for order {}", order.getId(), exception);
            order.getOrderItems().forEach(p -> stockGateway.release(p.getSku(), p.getAmount()));
            order.definePaymentNotApproved();
        }
    }
}
