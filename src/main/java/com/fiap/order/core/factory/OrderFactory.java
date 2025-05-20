package com.fiap.order.core.factory;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.valueobject.Address;

import java.util.Set;

public abstract class OrderFactory {
    private OrderFactory() {
        // Private constructor to prevent instantiation
    }

    public static Order build(String customerId, Set<OrderItem> orderItems, Address address) {
        return new Order(customerId, orderItems, address);
    }
}
