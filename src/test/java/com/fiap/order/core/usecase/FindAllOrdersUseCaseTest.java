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
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllOrdersUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private FindAllOrdersUseCase findAllOrdersUseCase;

    @Test
    void shouldReturnAllOrders() {
        var itemOrder1 = new OrderItem("sku1", 2, BigDecimal.TEN);
        var itemOrder2 = new OrderItem("sku2", 4, BigDecimal.TWO);

        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

        var order1 = new Order(
                "orderId",
                "costumerId",
                Set.of(itemOrder1),
                address,
                Status.AWAITING_PAYMENT,
                Instant.now()
        );

        var order2 = new Order(
                "orderId2",
                "costumerId",
                Set.of(itemOrder2),
                address,
                Status.AWAITING_PAYMENT,
                Instant.now()
        );

        when(orderGateway.findAll()).thenReturn(Set.of(order1, order2));

        var result = findAllOrdersUseCase.execute();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }
}
