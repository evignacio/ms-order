package com.fiap.order.infrastructure.mapper;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.infrastructure.repository.model.OrderItemModel;
import com.fiap.order.infrastructure.repository.model.OrderModel;

import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {

    }

    public static Order toEntity(OrderModel model) {
        var orderItems = model.getOrderItems()
                .stream()
                .map(item -> new OrderItem(item.getSku(), item.getAmount(), item.getValue()))
                .collect(Collectors.toSet());

        return new Order(
                model.getId(),
                model.getCustomerId(),
                orderItems,
                model.getAddress(),
                model.getStatus(),
                model.getCreatedAt()
        );
    }

    public static OrderModel toModel(Order order) {
        var orderItems = order.getOrderItems()
                .stream()
                .map(item -> new OrderItemModel(item.getSku(), item.getAmount(), item.getValue()))
                .collect(Collectors.toSet());

        return new OrderModel(
                order.getId(),
                order.getCustomerId(),
                orderItems,
                order.getAddress(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
