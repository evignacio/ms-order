package com.fiap.order.core.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class OrderTest {

    @Test
    void shouldCreateOrder() {
        var order = new Order(
                "123456",
                "123456789",
                Set.of(new Product("123456", 2, BigDecimal.TEN)),
                Status.PENDING,
                Instant.now()
        );

        assertThat(order.getId()).isEqualTo("123456");
        assertThat(order.getCustomerId()).isEqualTo("123456789");
        assertThat(order.getProducts()).isNotNull();
        assertThat(order.getProducts().size()).isEqualTo(1);
        assertThat(order.getStatus()).isEqualTo(Status.PENDING);
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldReturnCostumerIdNull() {
        var exception = catchThrowable(
                () -> new Order(
                        "123456",
                        null,
                        Set.of(new Product("123456", 2, BigDecimal.TEN)),
                        Status.PENDING,
                        Instant.now()
                )
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer ID cannot be null or empty");
    }

    @Test
    void shouldReturnProductsNullOrEmpty() {
        var exception = catchThrowable(
                () -> new Order(
                        "123456",
                        "123456789",
                        null,
                        Status.PENDING,
                        Instant.now()
                )
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Products cannot be null or empty");
    }

    @Test
    void shouldReturnStatusNull() {
        var exception = catchThrowable(
                () -> new Order(
                        "123456",
                        "123456789",
                        Set.of(new Product("123456", 2, BigDecimal.TEN)),
                        null,
                        Instant.now()
                )
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Status cannot be null");
    }

    @Test
    void shouldReturnCreateDateNull() {
        var exception = catchThrowable(
                () -> new Order(
                        "123456",
                        "123456789",
                        Set.of(new Product("123456", 2, BigDecimal.TEN)),
                        Status.PENDING,
                        null
                )
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Created At cannot be null");
    }
}
