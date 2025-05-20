package com.fiap.order.infrastructure.mapper;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.infrastructure.repository.model.AddressModel;
import com.fiap.order.infrastructure.repository.model.OrderItemModel;
import com.fiap.order.infrastructure.repository.model.OrderModel;

import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {

    }

    public static Order toEntity(OrderModel model) {
        var address = Address.AddressBuilder.builder()
                .name(model.getAddress().getName())
                .number(model.getAddress().getNumber())
                .build(
                        model.getAddress().getStreet(),
                        model.getAddress().getCity(),
                        model.getAddress().getState(),
                        model.getAddress().getCountry(),
                        model.getAddress().getZipCode()
                );

        var orderItems = model.getOrderItems()
                .stream()
                .map(item -> new OrderItem(item.getSku(), item.getAmount(), item.getValue()))
                .collect(Collectors.toSet());

        return new Order(
                model.getId(),
                model.getCustomerId(),
                orderItems,
                address,
                model.getStatus(),
                model.getCreatedAt()
        );
    }

    public static OrderModel toModel(Order entity) {
        var address = AddressModel.builder()
                .name(entity.getAddress().getName())
                .number(entity.getAddress().getNumber())
                .street(entity.getAddress().getStreet())
                .city(entity.getAddress().getCity())
                .state(entity.getAddress().getState())
                .country(entity.getAddress().getCountry())
                .zipCode(entity.getAddress().getZipCode())
                .build();

        var orderItems = entity.getOrderItems()
                .stream()
                .map(item -> new OrderItemModel(item.getSku(), item.getAmount(), item.getValue()))
                .collect(Collectors.toSet());

        return new OrderModel(
                entity.getId(),
                entity.getCustomerId(),
                orderItems,
                address,
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
