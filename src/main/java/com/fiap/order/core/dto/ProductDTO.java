package com.fiap.order.core.dto;

import java.math.BigDecimal;

public record ProductDTO(String sku, int amount, BigDecimal value) {
}
