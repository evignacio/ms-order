package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.dto.ProductDTO;
import com.fiap.order.core.gateway.ProductGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ProductGatewayImpl implements ProductGateway {
    @Override
    public Optional<ProductDTO> find(String sku) {
        return Optional.empty();
    }
}
