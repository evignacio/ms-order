package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.dto.ProductDTO;
import com.fiap.order.core.gateway.ProductGateway;
import com.fiap.order.infrastructure.integration.rest.ProductRestClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ProductGatewayImpl implements ProductGateway {

    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found, SKU: {}";

    private final ProductRestClient productRestClient;

    public ProductGatewayImpl(ProductRestClient productRestClient) {
        this.productRestClient = productRestClient;
    }

    @Override
    public Optional<ProductDTO> find(String sku) {
        log.info("Fetching product with SKU {}", sku);
        try {
            var productResponse = productRestClient.find(sku).getBody();
            if (productResponse == null) {
                log.warn(PRODUCT_NOT_FOUND_MESSAGE, sku);
                return Optional.empty();
            }
            return Optional.of(new ProductDTO(productResponse.sku(), productResponse.price()));
        } catch (FeignException exception) {
            if (HttpStatus.NO_CONTENT.value() == exception.status() || HttpStatus.NOT_FOUND.value() == exception.status()) {
                log.warn(PRODUCT_NOT_FOUND_MESSAGE, sku);
                return Optional.empty();
            }
            log.error("Error fetching product with SKU {}", sku, exception);
            throw exception;
        }
    }
}
