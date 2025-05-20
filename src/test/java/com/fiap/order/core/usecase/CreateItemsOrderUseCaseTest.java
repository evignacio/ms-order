package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.OrderItemDTO;
import com.fiap.order.core.dto.ProductDTO;
import com.fiap.order.core.gateway.ProductGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateItemsOrderUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private CreateItemsOrderUseCase createItemsOrderUseCase;

    @Test
    void shouldCreateOrderItemsSuccessfully() {
        var input = Set.of(new OrderItemDTO("sku1", 2), new OrderItemDTO("sku2", 1));

        when(productGateway.find("sku1")).thenReturn(Optional.of(new ProductDTO("sku1", BigDecimal.TEN)));
        when(productGateway.find("sku2")).thenReturn(Optional.of(new ProductDTO("sku2",  BigDecimal.ONE)));

        var result = createItemsOrderUseCase.execute(input);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        var item1 = result.stream().filter(item -> item.getSku().equals("sku1")).findFirst().orElseThrow();
        assertThat(item1.getSku()).isEqualTo("sku1");
        assertThat(item1.getAmount()).isEqualTo(2);
        assertThat(item1.getValue()).isEqualTo(BigDecimal.TEN);

        var item2 = result.stream().filter(item -> item.getSku().equals("sku2")).findFirst().orElseThrow();
        assertThat(item2.getSku()).isEqualTo("sku2");
        assertThat(item2.getAmount()).isEqualTo(1);
        assertThat(item2.getValue()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        var input = Set.of(new OrderItemDTO("sku1", 2));

        when(productGateway.find(anyString())).thenReturn(Optional.empty());

        var exception = catchThrowable(() -> createItemsOrderUseCase.execute(input));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product not found, sku:sku1");
    }
}