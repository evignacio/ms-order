package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.infrastructure.repository.OrderRepository;
import com.fiap.order.infrastructure.repository.model.AddressModel;
import com.fiap.order.infrastructure.repository.model.OrderItemModel;
import com.fiap.order.infrastructure.repository.model.OrderModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderGatewayImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderGatewayImpl orderGatewayImpl;

    @Test
    void shouldSaveOrder() {
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

        var addressModel = AddressModel.builder()
                .name("Name")
                .number("123")
                .street("Rua 1")
                .city("Sao Paulo")
                .state("Sao Paulo")
                .country("Brazil")
                .zipCode("01234567")
                .build();

        var ItemOrderModel = new OrderItemModel(
                "sku",
                2,
                BigDecimal.TEN
        );

        var orderModel = new OrderModel(
                "123456",
                "costumerId",
                Set.of(ItemOrderModel),
                addressModel,
                Status.AWAITING_PAYMENT,
                Instant.now()
        );

        when(orderRepository.save(any(OrderModel.class))).thenReturn(orderModel);

        var result = orderGatewayImpl.save(order);

        assertThat(result).isEqualTo(order);
        verify(orderRepository, times(1)).save(any(OrderModel.class));
    }

    @Test
    void shouldFindOrderById() {
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

        var addressModel = AddressModel.builder()
                .name("Name")
                .number("123")
                .street("Rua 1")
                .city("Sao Paulo")
                .state("Sao Paulo")
                .country("Brazil")
                .zipCode("01234567")
                .build();

        var ItemOrderModel = new OrderItemModel(
                "sku",
                2,
                BigDecimal.TEN
        );

        var orderModel = new OrderModel(
                "123456",
                "costumerId",
                Set.of(ItemOrderModel),
                addressModel,
                Status.AWAITING_PAYMENT,
                Instant.now()
        );

        when(orderRepository.findById("123456")).thenReturn(Optional.of(orderModel));

        var result = orderGatewayImpl.find("123456");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(order);
        verify(orderRepository).findById("123456");
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        var orderId = "orderId";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        var result = orderGatewayImpl.find(orderId);

        assertThat(result).isEmpty();
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldFindAllOrders() {
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

        var addressModel = AddressModel.builder()
                .name("Name")
                .number("123")
                .street("Rua 1")
                .city("Sao Paulo")
                .state("Sao Paulo")
                .country("Brazil")
                .zipCode("01234567")
                .build();

        var ItemOrderModel = new OrderItemModel(
                "sku",
                2,
                BigDecimal.TEN
        );

        var orderModel = new OrderModel(
                "123456",
                "costumerId",
                Set.of(ItemOrderModel),
                addressModel,
                Status.AWAITING_PAYMENT,
                Instant.now()
        );

        when(orderRepository.findAll()).thenReturn(Stream.of(orderModel).collect(Collectors.toList()));

        var result = orderGatewayImpl.findAll();

        assertThat(result).containsExactlyInAnyOrder(order);
        verify(orderRepository).findAll();
    }

    @Test
    void shouldDeleteOrderById() {
        var orderId = "orderId";

        doNothing().when(orderRepository).deleteById(orderId);

        orderGatewayImpl.delete(orderId);

        verify(orderRepository).deleteById(orderId);
    }
}