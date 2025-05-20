package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.*;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.factory.OrderFactory;
import com.fiap.order.core.gateway.*;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlterOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private CreateItemsOrderUseCase createItemsOrderUseCase;

    @Mock
    private ReserveStockUseCase reserveStockUseCase;

    @Mock
    private RegisterPaymentRequestUseCase registerPaymentRequestUseCase;

    @InjectMocks
    private AlterOrderUseCase alterOrderUseCase;

    @Test
    void shouldAlterOrderWithPayment() {
        try (MockedStatic<OrderFactory> mockedStatic = mockStatic(OrderFactory.class)) {
            var creditCard = new CreditCardDTO("token", "902");
            var input = new AlterOrderDTO("orderId", "addressId", Set.of(new OrderItemDTO("sku", 2)), creditCard);

            var address = Address.AddressBuilder.builder()
                    .name("Name")
                    .number("123")
                    .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

            var itemOrder = new OrderItem("sku", 2, BigDecimal.TEN);

            var order = new Order(
                    "123456",
                    "customerId",
                    Set.of(itemOrder),
                    address,
                    Status.AWAITING_PAYMENT,
                    Instant.now()
            );

            when(orderGateway.find(input.orderId())).thenReturn(Optional.of(order));
            when(customerGateway.findAddress(anyString(), anyString())).thenReturn(Optional.of(address));
            when(createItemsOrderUseCase.execute(input.orderItems())).thenReturn(Set.of(itemOrder));

            mockedStatic.when(() -> OrderFactory.build(anyString(), any(Set.class), any(Address.class)))
                    .thenReturn(order);

            doNothing().when(reserveStockUseCase).execute(any(Order.class));
            doNothing().when(registerPaymentRequestUseCase).execute(any(CreditCardDTO.class), any(Order.class));

            when(orderGateway.save(any(Order.class))).thenReturn(order);

            var result = alterOrderUseCase.execute(input);

            verify(registerPaymentRequestUseCase, times(1)).execute(any(CreditCardDTO.class), any(Order.class));
            verify(reserveStockUseCase, times(1)).execute(any(Order.class));
            verify(orderGateway, times(1)).save(any(Order.class));

            assertThat(result.getId()).isNotNull();
            assertThat(result.getCustomerId()).isEqualTo("customerId");
            assertThat(result.getOrderItems()).isNotNull();
            assertThat(result.getOrderItems()).hasSize(1);
            assertThat(result.getOrderItems().iterator().next().getSku()).isEqualTo("sku");
            assertThat(result.getOrderItems().iterator().next().getAmount()).isEqualTo(2);
            assertThat(result.getOrderItems().iterator().next().getValue()).isEqualTo(BigDecimal.TEN);
            assertThat(result.getStatus()).isEqualTo(Status.AWAITING_PAYMENT);
            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getAddress()).isNotNull();
            assertThat(result.getAddress().getName()).isEqualTo("Name");
            assertThat(result.getAddress().getStreet()).isEqualTo("Rua 1");
            assertThat(result.getAddress().getNumber()).isEqualTo("123");
            assertThat(result.getAddress().getCity()).isEqualTo("Sao Paulo");
            assertThat(result.getAddress().getCountry()).isEqualTo("Brazil");
            assertThat(result.getAddress().getZipCode()).isEqualTo("01234567");
        }
    }

    @Test
    void shouldAlterOrderWithOutPayment() {
        try (MockedStatic<OrderFactory> mockedStatic = mockStatic(OrderFactory.class)) {
            var creditCard = new CreditCardDTO("token", "902");
            var input = new AlterOrderDTO("orderId", "addressId", Set.of(new OrderItemDTO("sku", 2)), creditCard);

            var address = Address.AddressBuilder.builder()
                    .name("Name")
                    .number("123")
                    .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

            var itemOrders = Set.of(new OrderItem("sku", 2, BigDecimal.TEN),
                    new OrderItem("sku2", 1, BigDecimal.ONE));

            var order = new Order(
                    "123456",
                    "customerId",
                    itemOrders,
                    address,
                    Status.NO_STOCK,
                    Instant.now()
            );

            when(orderGateway.find(input.orderId())).thenReturn(Optional.of(order));
            when(customerGateway.findAddress(anyString(), anyString())).thenReturn(Optional.of(address));
            when(createItemsOrderUseCase.execute(input.orderItems())).thenReturn(itemOrders);

            mockedStatic.when(() -> OrderFactory.build(anyString(), any(Set.class), any(Address.class)))
                    .thenReturn(order);

            doNothing().when(reserveStockUseCase).execute(any(Order.class));

            when(orderGateway.save(any(Order.class))).thenReturn(order);

            var result = alterOrderUseCase.execute(input);

            verify(registerPaymentRequestUseCase, times(0)).execute(any(CreditCardDTO.class), any(Order.class));
            verify(reserveStockUseCase, times(1)).execute(any(Order.class));
            verify(orderGateway, times(1)).save(any(Order.class));

            assertThat(result.getId()).isNotNull();
            assertThat(result.getCustomerId()).isEqualTo("customerId");
            assertThat(result.getOrderItems()).isNotNull();
            assertThat(result.getOrderItems()).hasSize(2);
            assertThat(result.getStatus()).isEqualTo(Status.NO_STOCK);
            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getAddress()).isNotNull();
            assertThat(result.getAddress().getName()).isEqualTo("Name");
            assertThat(result.getAddress().getStreet()).isEqualTo("Rua 1");
            assertThat(result.getAddress().getNumber()).isEqualTo("123");
            assertThat(result.getAddress().getCity()).isEqualTo("Sao Paulo");
            assertThat(result.getAddress().getCountry()).isEqualTo("Brazil");
            assertThat(result.getAddress().getZipCode()).isEqualTo("01234567");
        }
    }

    @Test
    void shouldReturnOrderNotFound() {
        var creditCard = new CreditCardDTO("token", "902");
        var input = new AlterOrderDTO("orderId", "addressId", Set.of(new OrderItemDTO("sku", 2)), creditCard);

        when(orderGateway.find(input.orderId())).thenReturn(Optional.empty());
        var exception = catchThrowable(() -> alterOrderUseCase.execute(input));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order not found, id:orderId");
    }
    @ValueSource(strings = {"CANCELED", "COMPLETED"})
    @ParameterizedTest
    void shouldReturnStatusNotAllowOrderChanges(String status) {
        var creditCard = new CreditCardDTO("token", "902");
        var input = new AlterOrderDTO("orderId", "addressId", Set.of(new OrderItemDTO("sku", 2)), creditCard);

        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

        var itemOrders = Set.of(new OrderItem("sku", 2, BigDecimal.TEN),
                new OrderItem("sku2", 1, BigDecimal.ONE));

        var order = new Order(
                "123456",
                "customerId",
                itemOrders,
                address,
                Status.valueOf(status),
                Instant.now()
        );

        when(orderGateway.find(input.orderId())).thenReturn(Optional.of(order));

        var exception =  catchThrowable(() -> alterOrderUseCase.execute(input));
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order status does not allow changes");
    }
}
