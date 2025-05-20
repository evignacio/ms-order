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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private DeleteOrderUseCase deleteOrderUseCase;

    @Test
    void shouldDeleteOrder() {
        var orderId = "orderId";

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

        when(orderGateway.find(orderId)).thenReturn(Optional.of(order));
        deleteOrderUseCase.execute(orderId);

        verify(orderGateway).delete(orderId);
    }

    @Test
    void shouldReturnOrderNotFound() {
        var orderId = "orderId";

        when(orderGateway.find(orderId)).thenReturn(Optional.empty());

        var exception = catchThrowable(() -> deleteOrderUseCase.execute(orderId));

        assertThat(exception).isInstanceOf(IllegalStateException.class)
                .hasMessage("Order not found, id: " + orderId);
    }
}
