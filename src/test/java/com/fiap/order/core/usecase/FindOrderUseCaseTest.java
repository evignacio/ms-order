package com.fiap.order.core.usecase;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.gateway.OrderGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private FindOrderUseCase findOrderUseCase;

    @Test
    void shouldReturnOrder() {
        var itemOrder = new OrderItem("sku", 2, BigDecimal.TEN);
        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

        var order = new Order(
                "orderId",
                "customerId",
                Set.of(itemOrder),
                address,
                Status.AWAITING_PAYMENT,
                Instant.now()
        );
        when(orderGateway.find("orderId")).thenReturn(Optional.of(order));

        var result = findOrderUseCase.execute("orderId");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("orderId");
    }

    @Test
    void shouldReturnOrderNotFound() {
        when(orderGateway.find("orderId")).thenReturn(Optional.empty());

        var exception = catchThrowable(() -> findOrderUseCase.execute("orderId"));

        assertThat(exception.getMessage()).isEqualTo("Order not found, id: orderId");
    }
}
