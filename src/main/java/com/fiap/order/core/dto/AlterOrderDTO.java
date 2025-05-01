package com.fiap.order.core.dto;

import java.util.Set;

public record AlterOrderDTO(String orderId, String addressId, Set<OrderItemDTO> orderItems, CreditCardDTO creditCard) {
}
