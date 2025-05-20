package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.CreditCardDTO;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.entity.OrderItem;
import com.fiap.order.core.entity.Status;
import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.exception.PaymentNotApprovedException;
import com.fiap.order.core.gateway.PaymentGateway;
import com.fiap.order.core.gateway.StockGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterPaymentRequestUseCaseTest {

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private ReleaseStockUseCase releaseStockUseCase;

    @InjectMocks
    private RegisterPaymentRequestUseCase registerPaymentRequestUseCase;

    @Test
    void shouldRegisterPaymentSuccessfully() {
        var creditCard = new CreditCardDTO("token", "902");
        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");
        var orderItem = new OrderItem("sku", 2, BigDecimal.TEN);
        var order = new Order("123", "customerId", Set.of(orderItem), address, Status.AWAITING_PAYMENT, Instant.now());

        registerPaymentRequestUseCase.execute(creditCard, order);

        verify(paymentGateway, times(1)).registerPaymentRequest(creditCard, order);
        assertThat(order.getStatus()).isEqualTo(Status.PROCESSING_PAYMENT);
    }

    @Test
    void shouldHandlePaymentNotApprovedException() {
        var creditCard = new CreditCardDTO("token", "902");
        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");
        var orderItem = new OrderItem("sku", 2, BigDecimal.TEN);
        var order = new Order("123", "customerId", Set.of(orderItem), address, Status.AWAITING_PAYMENT, Instant.now());

        doThrow(new PaymentNotApprovedException("Payment not approved")).when(paymentGateway).registerPaymentRequest(creditCard, order);
        doNothing().when(releaseStockUseCase).execute(order);

        registerPaymentRequestUseCase.execute(creditCard, order);

        verify(releaseStockUseCase, times(1)).execute(order);
        assertThat(order.getStatus()).isEqualTo(Status.PAYMENT_NOT_APPROVED);
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotAvailable() {
        var creditCard = new CreditCardDTO("token", "902");
        var address = Address.AddressBuilder.builder()
                .name("Name")
                .number("123")
                .build("Rua 1", "Sao Paulo", "Sao Paulo", "Brazil", "01234567");
        var orderItem = new OrderItem("sku", 2, BigDecimal.TEN);
        var order = new Order("123", "customerId", Set.of(orderItem), address, Status.PENDING, Instant.now());

        var exception = catchThrowable(() -> registerPaymentRequestUseCase.execute(creditCard, order));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Payment not available for order 123");
        verifyNoInteractions(paymentGateway);
        verifyNoInteractions(releaseStockUseCase);
    }
}