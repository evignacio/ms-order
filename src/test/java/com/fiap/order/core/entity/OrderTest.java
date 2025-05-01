package com.fiap.order.core.entity;

import com.fiap.order.core.entity.valueobject.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class OrderTest {

    private Address address;

    @BeforeEach
    void setUp() {
        address = Address.AddressBuilder.builder()
                .build("Rua 1", "123", "SP", "Brazil", "01234567");
    }

    @Test
    void shouldCreateOrder() {
        var order = new Order(
                "123456",
                "123456789",
                Set.of(new OrderItem("123456", 2, BigDecimal.TEN)),
                address,
                Status.PENDING,
                Instant.now()
        );

        assertThat(order.getId()).isEqualTo("123456");
        assertThat(order.getCustomerId()).isEqualTo("123456789");
        assertThat(order.getOrderItems()).isNotNull();
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        assertThat(order.getStatus()).isEqualTo(Status.PENDING);
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldReturnCostumerIdNull() {
        var exception = catchThrowable(
                () -> new Order(
                        "123456",
                        null,
                        Set.of(new OrderItem("123456", 2, BigDecimal.TEN)),
                        address,
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
                        address,
                        Status.PENDING,
                        Instant.now()
                )
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("OrderItems cannot be null or empty");
    }

    @Test
    void shouldReturnStatusNull() {
        var exception = catchThrowable(
                () -> new Order(
                        "123456",
                        "123456789",
                        Set.of(new OrderItem("123456", 2, BigDecimal.TEN)),
                        address,
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
                        Set.of(new OrderItem("123456", 2, BigDecimal.TEN)),
                        address,
                        Status.PENDING,
                        null
                )
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Created At cannot be null");
    }

    @Test
    void shouldReturnTotalValue() {
        var product1 = new OrderItem("123456", 2, BigDecimal.TEN);
        var product2 = new OrderItem("123457", 2, BigDecimal.ONE);

        var order = new Order(
                "123456",
                "123456789",
                Set.of(product1, product2),
                address,
                Status.PENDING,
                Instant.now()
        );

        assertThat(order.getTotalValue()).isEqualTo(BigDecimal.valueOf(22));
    }

    @Test
    void shouldReturnStatusNoStock() {
        var product1 = new OrderItem("123456", 2, BigDecimal.TEN);
        var product2 = new OrderItem("123457", 2, BigDecimal.ONE);

        var order = new Order(
                "123456",
                "123456789",
                Set.of(product1, product2),
                address,
                Status.PENDING,
                Instant.now()
        );

        order.defineNoStock();

        assertThat(order.getStatus()).isEqualTo(Status.NO_STOCK);
    }

    @Test
    void shouldReturnStatusPaymentNotApproved() {
        var product1 = new OrderItem("123456", 2, BigDecimal.TEN);
        var product2 = new OrderItem("123457", 2, BigDecimal.ONE);

        var order = new Order(
                "123456",
                "123456789",
                Set.of(product1, product2),
                address,
                Status.PENDING,
                Instant.now()
        );

        order.definePaymentNotApproved();

        assertThat(order.getStatus()).isEqualTo(Status.PAYMENT_NOT_APPROVED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"AWAITING_PAYMENT", "PAYMENT_NOT_APPROVED"})
    void shouldReturnPaymentAvailable(String status) {
        var product1 = new OrderItem("123456", 2, BigDecimal.TEN);
        var product2 = new OrderItem("123457", 2, BigDecimal.ONE);

        var order = new Order(
                "123456",
                "123456789",
                Set.of(product1, product2),
                address,
                Status.valueOf(status),
                Instant.now()
        );

        assertThat(order.isPaymentAvailable()).isTrue();
    }

    @ValueSource(strings = {"CANCELED", "COMPLETED"})
    @ParameterizedTest
    void shouldReturnClosed(String status) {
            var product1 = new OrderItem("123456", 2, BigDecimal.TEN);
            var product2 = new OrderItem("123457", 2, BigDecimal.ONE);

            var order = new Order(
                    "123456",
                    "123456789",
                    Set.of(product1, product2),
                    address,
                    Status.valueOf(status),
                    Instant.now()
            );

            assertThat(order.isClosed()).isTrue();
    }
}
