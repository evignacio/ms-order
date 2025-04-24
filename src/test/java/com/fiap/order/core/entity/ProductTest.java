package com.fiap.order.core.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class ProductTest {

    @Test
    void shouldCreateProduct() {
        var product = new Product("123456", 2, BigDecimal.TEN);

        assertThat(product.getSku()).isEqualTo("123456");
        assertThat(product.getAmount()).isEqualTo(2);
        assertThat(product.getPrice()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldReturnSkuNull() {
        var exception = catchThrowable(
                () -> new Product(null, 2, BigDecimal.TEN)
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("SKU cannot be null or empty");
    }

    @Test
    void shouldReturnInvalidAmount() {
        var exception = catchThrowable(
                () -> new Product("123456", 0, BigDecimal.TEN)
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
    }

    @Test
    void shoudlReturnInvalidPrice() {
        var exception = catchThrowable(
                () -> new Product("123456", 2, null)
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price cannot be null or negative");
    }
}
