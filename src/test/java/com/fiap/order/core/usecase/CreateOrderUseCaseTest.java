package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.*;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.exception.PaymentNotApprovedException;
import com.fiap.order.core.exception.StockNotAvailableException;
import com.fiap.order.core.gateway.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private ProductGateway productGateway;

    @Mock
    private StockGateway stockGateway;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;


    @Test
    void shouldCreateOrderWithPayment() {
        var item = new OrderItemDTO("sku", 2);
        var creditCard = new CreditCardDTO("token", "902");
        var customer = new CustomerDTO("costumerId", "addressId");
        var input = new CreateOrderDTO(customer, Set.of(item), creditCard);

        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

        var product = new ProductDTO("sku", BigDecimal.TEN);

        var order = new Order(
                "123456",
                "costumerId",
                Set.of(new OrderItem("sku", 2, BigDecimal.TEN)),
                address,
                Status.PENDING,
                Instant.now()
        );

        when(customerGateway.findAddress(customer.costumerId(), customer.addressId()))
                .thenReturn(Optional.of(address));

        when(productGateway.find(item.sku()))
                .thenReturn(Optional.of(product));

        doNothing().when(stockGateway).reserve("sku", 2);
        doNothing().when(paymentGateway).registerPaymentRequest(any(CreditCardDTO.class), any(Order.class));

        when(orderGateway.save(any(Order.class))).thenReturn(order);

        var result = createOrderUseCase.execute(input);

        verify(paymentGateway, times(1)).registerPaymentRequest(any(CreditCardDTO.class), any(Order.class));
        verify(stockGateway, times(1)).reserve("sku", 2);
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("costumerId");
        assertThat(result.getOrderItems()).isNotNull();
        assertThat(result.getOrderItems()).hasSize(1);
        assertThat(result.getOrderItems().iterator().next().getSku()).isEqualTo("sku");
        assertThat(result.getOrderItems().iterator().next().getAmount()).isEqualTo(2);
        assertThat(result.getOrderItems().iterator().next().getValue()).isEqualTo(BigDecimal.TEN);
        assertThat(result.getStatus()).isEqualTo(Status.PENDING);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getAddress()).isNotNull();
        assertThat(result.getAddress().getName()).isEqualTo("Name");
        assertThat(result.getAddress().getStreet()).isEqualTo("Rua 1");
        assertThat(result.getAddress().getNumber()).isEqualTo("123");
        assertThat(result.getAddress().getCity()).isEqualTo("Sao Paulo");
        assertThat(result.getAddress().getCountry()).isEqualTo("Brazil");
        assertThat(result.getAddress().getZipCode()).isEqualTo("01234567");
    }

    @Test
    void shouldCreateOrderWithStatusClosedNoStock() {
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

        var product = new ProductDTO("sku", BigDecimal.TEN);
        var product2 = new ProductDTO("sku2", BigDecimal.ONE);

        var order = new Order(
                "123456",
                "costumerId",
                Set.of(new OrderItem("sku", 2, BigDecimal.TEN),
                        new OrderItem("sku2", 1, BigDecimal.ONE)),
                address,
                Status.PENDING,
                Instant.now()
        );

        when(customerGateway.findAddress(customer.costumerId(), customer.addressId()))
                .thenReturn(Optional.of(address));

        when(productGateway.find(item.sku()))
                .thenReturn(Optional.of(product));

        when(productGateway.find(item2.sku()))
                .thenReturn(Optional.of(product2));

        doNothing().when(stockGateway).reserve("sku", 2);
        doThrow(new StockNotAvailableException("No stock for product")).when(stockGateway).reserve("sku2", 2);

        when(orderGateway.save(any(Order.class))).thenReturn(order);

        var result = createOrderUseCase.execute(input);

        verify(paymentGateway, times(0)).registerPaymentRequest(any(CreditCardDTO.class), any(Order.class));
        verify(stockGateway, times(2)).reserve(anyString(), anyInt());
        verify(stockGateway, times(1)).release("sku", 2);
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("costumerId");
        assertThat(result.getOrderItems()).isNotNull();
        assertThat(result.getOrderItems()).hasSize(2);
        assertThat(result.getStatus()).isEqualTo(Status.CLOSED_NO_STOCK);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getAddress()).isNotNull();
        assertThat(result.getAddress().getName()).isEqualTo("Name");
        assertThat(result.getAddress().getStreet()).isEqualTo("Rua 1");
        assertThat(result.getAddress().getNumber()).isEqualTo("123");
        assertThat(result.getAddress().getCity()).isEqualTo("Sao Paulo");
        assertThat(result.getAddress().getCountry()).isEqualTo("Brazil");
        assertThat(result.getAddress().getZipCode()).isEqualTo("01234567");
    }

    @Test
    void shouldCreateOrderWithStatusClosedPaymentNotApproved() {
        var item = new OrderItemDTO("sku", 2);
        var creditCard = new CreditCardDTO("token", "902");
        var customer = new CustomerDTO("costumerId", "addressId");
        var input = new CreateOrderDTO(customer, Set.of(item), creditCard);

        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");

        var product = new ProductDTO("sku", BigDecimal.TEN);

        var order = new Order(
                "123456",
                "costumerId",
                Set.of(new OrderItem("sku", 2, BigDecimal.TEN)),
                address,
                Status.PENDING,
                Instant.now()
        );

        when(customerGateway.findAddress(customer.costumerId(), customer.addressId()))
                .thenReturn(Optional.of(address));

        when(productGateway.find(item.sku()))
                .thenReturn(Optional.of(product));

        doNothing().when(stockGateway).reserve("sku", 2);
        doThrow(new PaymentNotApprovedException("Payment not approved")).when(paymentGateway).registerPaymentRequest(any(CreditCardDTO.class), any(Order.class));

        when(orderGateway.save(any(Order.class))).thenReturn(order);

        var result = createOrderUseCase.execute(input);

        verify(paymentGateway, times(1)).registerPaymentRequest(any(CreditCardDTO.class), any(Order.class));
        verify(stockGateway, times(1)).reserve("sku", 2);
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("costumerId");
        assertThat(result.getOrderItems()).isNotNull();
        assertThat(result.getOrderItems()).hasSize(1);
        assertThat(result.getOrderItems().iterator().next().getSku()).isEqualTo("sku");
        assertThat(result.getOrderItems().iterator().next().getAmount()).isEqualTo(2);
        assertThat(result.getOrderItems().iterator().next().getValue()).isEqualTo(BigDecimal.TEN);
        assertThat(result.getStatus()).isEqualTo(Status.CLOSED_PAYMENT_NOT_APPROVED);
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
