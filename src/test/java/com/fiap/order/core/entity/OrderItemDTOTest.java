package com.fiap.order.core.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class OrderItemDTOTest {

    @Test
    void shouldCreateProduct() {
        var product = new OrderItem("123456", 2, BigDecimal.TEN);

        assertThat(product.getSku()).isEqualTo("123456");
        assertThat(product.getAmount()).isEqualTo(2);
        assertThat(product.getValue()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldReturnSkuNull() {
        var exception = catchThrowable(
                () -> new OrderItem(null, 2, BigDecimal.TEN)
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SKU cannot be null or empty");
    }

    @Test
    void shouldReturnInvalidAmount() {
        var exception = catchThrowable(
                () -> new OrderItem("123456", 0, BigDecimal.TEN)
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
    }

    @Test
    void shoudlReturnInvalidPrice() {
        var exception = catchThrowable(
                () -> new OrderItem("123456", 2, null)
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Value cannot be null or negative");
    }

    @Test
    void shouldReturnTotalValue() {
        var product = new OrderItem("123456", 2, BigDecimal.TEN);

        assertThat(product.getTotalValue()).isEqualTo(BigDecimal.valueOf(20));
    }
}
