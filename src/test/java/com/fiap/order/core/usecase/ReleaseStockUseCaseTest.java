package com.fiap.order.core.usecase;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.gateway.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.mockito.Mockito.*;

class ReleaseStockUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    private ReleaseStockUseCase releaseStockUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        releaseStockUseCase = new ReleaseStockUseCase(stockGateway);
    }

    @Test
    void shouldReleaseStockForOrderItems() {
        var orderItems = Set.of(
                new OrderItem("sku1", 2, BigDecimal.TEN),
                new OrderItem("sku2", 1, BigDecimal.ONE)
        );

        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

        var order = new Order(
                "123456",
                "customerId",
                orderItems,
                address,
                Status.AWAITING_PAYMENT,
                Instant.now()
        );

        releaseStockUseCase.execute(order);

        verify(stockGateway, times(1)).release("sku1", 2);
        verify(stockGateway, times(1)).release("sku2", 1);
        verifyNoMoreInteractions(stockGateway);
    }
}