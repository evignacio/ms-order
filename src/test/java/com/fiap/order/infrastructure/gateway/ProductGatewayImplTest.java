package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.dto.ProductDTO;
import com.fiap.order.infrastructure.integration.rest.ProductRestClient;
import com.fiap.order.infrastructure.integration.rest.to.ProductResponse;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductGatewayImplTest {

    @Mock
    private ProductRestClient productRestClient;

    @InjectMocks
    private ProductGatewayImpl productGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnProductDTOWhenProductExists() {
        String sku = "12345";
        ProductResponse productResponse = new ProductResponse(sku, BigDecimal.valueOf(100.0));
        when(productRestClient.find(sku)).thenReturn(ResponseEntity.ok(productResponse));

        Optional<ProductDTO> result = productGateway.find(sku);

        assertTrue(result.isPresent());
        assertEquals(sku, result.get().sku());
        assertEquals(BigDecimal.valueOf(100.0), result.get().value());
        verify(productRestClient, times(1)).find(sku);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        String sku = "12345";
        when(productRestClient.find(sku)).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));

        Optional<ProductDTO> result = productGateway.find(sku);

        assertTrue(result.isEmpty());
        verify(productRestClient, times(1)).find(sku);
    }

    @Test
    void shouldReturnEmptyWhenFeignExceptionNotFound() {
        String sku = "12345";
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(HttpStatus.NOT_FOUND.value());
        when(productRestClient.find(sku)).thenThrow(feignException);

        Optional<ProductDTO> result = productGateway.find(sku);

        assertTrue(result.isEmpty());
        verify(productRestClient, times(1)).find(sku);
    }

    @Test
    void shouldThrowExceptionWhenFeignExceptionOtherThanNotFound() {
        String sku = "12345";
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(productRestClient.find(sku)).thenThrow(feignException);

        FeignException exception = assertThrows(FeignException.class, () -> productGateway.find(sku));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.status());
        verify(productRestClient, times(1)).find(sku);
    }
}