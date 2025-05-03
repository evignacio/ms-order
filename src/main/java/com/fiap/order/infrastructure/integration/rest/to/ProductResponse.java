package com.fiap.order.infrastructure.integration.rest.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductResponse(String sku, BigDecimal price) {
}
