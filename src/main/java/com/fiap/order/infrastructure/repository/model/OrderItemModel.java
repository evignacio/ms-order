package com.fiap.order.infrastructure.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemModel {
    private String sku;
    private int amount;
    private BigDecimal value;
}
