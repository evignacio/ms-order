package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.*;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.factory.OrderFactory;
import com.fiap.order.core.gateway.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

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
    private CreateOrderUseCase createOrderUseCase;


    @Test
    void shouldCreateOrderWithPayment() {
        try (MockedStatic<OrderFactory> mockedStatic = mockStatic(OrderFactory.class)) {
            var creditCard = new CreditCardDTO("token", "902");
            var customer = new CustomerDTO("costumerId", "addressId");
            var input = new CreateOrderDTO(customer, Set.of(new OrderItemDTO("sku", 2)), creditCard);

            var address = Address.AddressBuilder.builder()
                    .name("Name")
                    .number("123")
                    .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

            var itemOrder = new OrderItem("sku", 2, BigDecimal.TEN);

            var order = new Order(
                    "123456",
                    "costumerId",
                    Set.of(itemOrder),
                    address,
                    Status.AWAITING_PAYMENT,
                    Instant.now()
            );

            when(customerGateway.findAddress(customer.costumerId(), customer.addressId())).thenReturn(Optional.of(address));
            when(createItemsOrderUseCase.execute(input.orderItems())).thenReturn(Set.of(itemOrder));

            mockedStatic.when(() -> OrderFactory.build(anyString(), any(Set.class), any(Address.class)))
                    .thenReturn(order);

            doNothing().when(reserveStockUseCase).execute(any(Order.class));
            doNothing().when(registerPaymentRequestUseCase).execute(any(CreditCardDTO.class), any(Order.class));

            when(orderGateway.save(any(Order.class))).thenReturn(order);

            var result = createOrderUseCase.execute(input);

            verify(registerPaymentRequestUseCase, times(1)).execute(any(CreditCardDTO.class), any(Order.class));
            verify(reserveStockUseCase, times(1)).execute(any(Order.class));
            verify(orderGateway, times(1)).save(any(Order.class));

            assertThat(result.getId()).isNotNull();
            assertThat(result.getCustomerId()).isEqualTo("costumerId");
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
    void shouldCreateOrderWithOutPayment() {
        try (MockedStatic<OrderFactory> mockedStatic = mockStatic(OrderFactory.class)) {
            var items = new LinkedHashSet<OrderItemDTO>();
            var item = new OrderItemDTO("sku", 2);
            var item2 = new OrderItemDTO("sku2", 2);
            items.add(item);
            items.add(item2);

            var creditCard = new CreditCardDTO("token", "902");
            var customer = new CustomerDTO("costumerId", "addressId");
            var input = new CreateOrderDTO(customer, items, creditCard);

            var address = Address.AddressBuilder.builder()
                    .name("Name")
                    .number("123")
                    .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

            var itemOrders = Set.of(new OrderItem("sku", 2, BigDecimal.TEN),
                    new OrderItem("sku2", 1, BigDecimal.ONE));

            var order = new Order(
                    "123456",
                    "costumerId",
                    itemOrders,
                    address,
                    Status.NO_STOCK,
                    Instant.now()
            );

            when(customerGateway.findAddress(customer.costumerId(), customer.addressId())).thenReturn(Optional.of(address));
            when(createItemsOrderUseCase.execute(input.orderItems())).thenReturn(itemOrders);

            mockedStatic.when(() -> OrderFactory.build(anyString(), any(Set.class), any(Address.class)))
                    .thenReturn(order);

            doNothing().when(reserveStockUseCase).execute(any(Order.class));

            when(orderGateway.save(any(Order.class))).thenReturn(order);

            var result = createOrderUseCase.execute(input);

            verify(registerPaymentRequestUseCase, times(0)).execute(any(CreditCardDTO.class), any(Order.class));
            verify(reserveStockUseCase, times(1)).execute(any(Order.class));
            verify(orderGateway, times(1)).save(any(Order.class));

            assertThat(result.getId()).isNotNull();
            assertThat(result.getCustomerId()).isEqualTo("costumerId");
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
}
