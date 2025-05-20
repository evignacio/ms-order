package com.fiap.order.infrastructure.integration.listener;

import com.fiap.order.core.dto.CreateOrderDTO;
import com.fiap.order.core.usecase.CreateOrderUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderResquestListenerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @InjectMocks
    private OrderResquestListener orderResquestListener;

    @Test
    void shouldProcessOrderRequestSuccessfully() {
        var createOrderDTO = mock(CreateOrderDTO.class);

        orderResquestListener.processOrderRequest(createOrderDTO);

        verify(createOrderUseCase, times(1)).execute(createOrderDTO);
    }
}