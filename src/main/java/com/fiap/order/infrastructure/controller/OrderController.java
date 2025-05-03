package com.fiap.order.infrastructure.controller;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.usecase.FindAllOrdersUseCase;
import com.fiap.order.core.usecase.FindOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final FindAllOrdersUseCase findAllOrdersUseCase;
    private final FindOrderUseCase findOrderUseCase;

    public OrderController(FindAllOrdersUseCase findAllOrdersUseCase, FindOrderUseCase findOrderUseCase) {
        this.findAllOrdersUseCase = findAllOrdersUseCase;
        this.findOrderUseCase = findOrderUseCase;
    }

    @GetMapping
    public ResponseEntity<Set<Order>> findAllOrders() {
        return ResponseEntity.ok(findAllOrdersUseCase.execute());
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> findOrder(@PathVariable String id) {
        return ResponseEntity.ok(findOrderUseCase.execute(id));
    }
}
