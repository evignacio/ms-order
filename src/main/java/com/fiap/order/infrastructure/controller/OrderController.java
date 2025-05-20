package com.fiap.order.infrastructure.controller;

import com.fiap.order.core.dto.AlterOrderDTO;
import com.fiap.order.core.dto.PaymentStatus;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.usecase.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final FindAllOrdersUseCase findAllOrdersUseCase;
    private final FindOrderUseCase findOrderUseCase;
    private final AlterOrderUseCase alterOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final CompletePaymentRequestUseCase completePaymentRequestUseCase;


    public OrderController(FindAllOrdersUseCase findAllOrdersUseCase,
                           FindOrderUseCase findOrderUseCase,
                           AlterOrderUseCase alterOrderUseCase,
                           DeleteOrderUseCase deleteOrderUseCase,
                           CompletePaymentRequestUseCase completePaymentRequestUseCase) {
        this.findAllOrdersUseCase = findAllOrdersUseCase;
        this.findOrderUseCase = findOrderUseCase;
        this.alterOrderUseCase = alterOrderUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.completePaymentRequestUseCase = completePaymentRequestUseCase;
    }

    @GetMapping
    public ResponseEntity<Set<Order>> findAllOrders() {
        return ResponseEntity.ok(findAllOrdersUseCase.execute());
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> findOrder(@PathVariable String id) {
        return ResponseEntity.ok(findOrderUseCase.execute(id));
    }

    @PutMapping
    public ResponseEntity<Order> alterOrder(@RequestBody AlterOrderDTO order) {
        return ResponseEntity.ok(alterOrderUseCase.execute(order));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        deleteOrderUseCase.execute(id);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("{id}/payment/callback")
    public ResponseEntity<Void> completePaymentRequest(@PathVariable String id, @RequestHeader("payment-status") PaymentStatus paymentStatus) {
        completePaymentRequestUseCase.execute(id, paymentStatus);
        return ResponseEntity.ok()
                .build();
    }
}
