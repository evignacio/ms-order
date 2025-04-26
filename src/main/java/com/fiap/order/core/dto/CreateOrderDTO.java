package com.fiap.order.core.dto;

import java.util.Set;

public record CreateOrderDTO(String customerId, Set<ProductDTO> products, CreditCardDTO creditCard) {
}
