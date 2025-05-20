package com.fiap.order.core.usecase;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.exception.StockNotAvailableException;
import com.fiap.order.core.gateway.StockGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReserveStockUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    @InjectMocks
    private ReserveStockUseCase reserveStockUseCase;

    @Test
    void shouldReserveStockSuccessfully() {
        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");
        var orderItem1 = new OrderItem("sku1", 2, BigDecimal.TEN);
        var orderItem2 = new OrderItem("sku2", 1, BigDecimal.ONE);
        var order = new Order("123", "customerId", Set.of(orderItem1, orderItem2), address, Status.PENDING, Instant.now());

        reserveStockUseCase.execute(order);

        verify(stockGateway, times(1)).reserve("sku1", 2);
        verify(stockGateway, times(1)).reserve("sku2", 1);
        assertThat(order.getStatus()).isEqualTo(Status.AWAITING_PAYMENT);
    }

    @Test
    void shouldHandleStockNotAvailableException() {
        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");
        var orderItems = new LinkedHashSet<OrderItem>();
        orderItems.add(new OrderItem("sku1", 2, BigDecimal.TEN));
        orderItems.add(new OrderItem("sku2", 1, BigDecimal.ONE));
        var order = new Order("123", "customerId", orderItems, address, Status.PENDING, Instant.now());

        doNothing().when(stockGateway).reserve("sku1", 2);
        doThrow(new StockNotAvailableException("Stock not available")).when(stockGateway).reserve("sku2", 1);

        reserveStockUseCase.execute(order);

        verify(stockGateway, times(1)).reserve("sku1", 2);
        verify(stockGateway, times(1)).reserve("sku2", 1);
        verify(stockGateway, times(1)).release("sku1", 2);
        assertThat(order.getStatus()).isEqualTo(Status.NO_STOCK);
    }
}