package com.fiap.order.core.dto;

import java.util.Set;

public record CreateOrderDTO(CustomerDTO customer, Set<OrderItemDTO> orderItems, CreditCardDTO creditCard) {
}

