package com.fiap.order.core.gateway;

import com.fiap.order.core.dto.ProductDTO;

import java.util.Optional;

public interface ProductGateway {
    Optional<ProductDTO> find(String sku);
}
