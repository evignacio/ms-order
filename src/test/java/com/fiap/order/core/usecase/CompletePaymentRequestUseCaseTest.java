package com.fiap.order.core.usecase;

import com.fiap.order.core.dto.PaymentStatus;
import com.fiap.order.core.entity.Order;
import com.fiap.order.core.gateway.OrderGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CompletePaymentRequestUseCaseTest {

    @Mock
    private ReleaseStockUseCase releaseStockUseCase;

    @Mock
    private FindOrderUseCase findOrderUseCase;

    @Mock
    private OrderGateway orderGateway;

    private CompletePaymentRequestUseCase completePaymentRequestUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        completePaymentRequestUseCase = new CompletePaymentRequestUseCase(releaseStockUseCase, findOrderUseCase, orderGateway);
    }

    @Test
    void shouldCompletePaymentWhenPaymentIsApproved() {
        var orderId = "orderId";
        var order = mock(Order.class);

        when(findOrderUseCase.execute(orderId)).thenReturn(order);

        completePaymentRequestUseCase.execute(orderId, PaymentStatus.APPROVED);

        verify(order).defineCompleted();
        verify(orderGateway).save(order);
        verifyNoInteractions(releaseStockUseCase);
    }

    @Test
    void shouldReleaseStockAndMarkPaymentNotApprovedWhenPaymentIsNotApproved() {
        var orderId = "orderId";
        var order = mock(Order.class);

        when(findOrderUseCase.execute(orderId)).thenReturn(order);

        completePaymentRequestUseCase.execute(orderId, PaymentStatus.DECLINED);

        verify(releaseStockUseCase).execute(order);
        verify(order).definePaymentNotApproved();
        verify(orderGateway).save(order);
    }
}