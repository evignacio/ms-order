package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.CreateOrderDTO;
import com.fiap.order.core.entity.CreditCard;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.Product;
import com.fiap.order.core.exception.PaymentNotApprovedException;
import com.fiap.order.core.exception.StockNotAvailableException;
import com.fiap.order.core.gateway.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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

    public void execute(CreateOrderDTO input) {
        log.info("Creating order for customer {}", input.customerId());

        if (!customerGateway.exists(input.customerId()))
            throw new IllegalArgumentException("Customer not found customerId:" + input.customerId());

        var products = input.products()
                .stream()
                .map(p -> new Product(p.sku(), p.amount(), p.value()))
                .collect(Collectors.toSet());

        var order = new Order(input.customerId(), products);

        products.forEach(p -> {
            if (!productGateway.exists(p.getSku()))
                throw new IllegalArgumentException("Product not found sku:" + p.getSku());
        });

        reserveStock(order);
        if (order.isPending()) {
            var creditCard = new CreditCard(
                    input.creditCard().number(),
                    input.creditCard().name(),
                    input.creditCard().expirationDate(),
                    input.creditCard().cvv()
            );
            registerPaymentRequest(creditCard, order);
        }
        orderGateway.save(order);
        log.info("Order {} created", order.getId());
    }

    private void reserveStock(Order order) {
        order.getProducts().forEach(p -> {
            try {
                log.info("Reserving stock for product {}, amount {}", p.getSku(), p.getAmount());
                stockGateway.reserve(p.getSku(), p.getAmount());
            } catch (StockNotAvailableException exception) {
                log.error("Stock not available for product {}, amount {}", p.getSku(), p.getAmount(), exception);
                order.defineNoStock();
            }
        });
    }

    private void registerPaymentRequest(CreditCard creditCard, Order order) {
        try {
            log.info("Requesting payment for the order {}", order.getId());
            paymentGateway.registerPaymentRequest(creditCard, order);
        } catch (PaymentNotApprovedException exception) {
            log.error("Payment failed for order {}", order.getId(), exception);
            order.getProducts().forEach(p -> stockGateway.release(p.getSku(), p.getAmount()));
            order.definePaymentNotApproved();
        }
    }
}
