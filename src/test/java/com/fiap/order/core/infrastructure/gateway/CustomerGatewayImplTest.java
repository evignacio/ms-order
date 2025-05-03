package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.infrastructure.integration.rest.CustomerRestClient;
import com.fiap.order.infrastructure.integration.rest.to.AddressTO;
import com.fiap.order.infrastructure.integration.rest.to.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerGatewayImplTest {

    @Mock
    private CustomerRestClient customerRestClient;

    @InjectMocks
    private CustomerGatewayImpl customerGateway;

    @Test
    void shouldReturnAddressWhenCustomerAndAddressExist() {
        String customerId = "123";
        String addressId = "456";
        AddressTO addressTO = new AddressTO(addressId, "Home", "Main St", "City", "State", "12345", 100);
        CustomerResponse customerResponse = new CustomerResponse(customerId, Set.of(addressTO));
        when(customerRestClient.find(customerId)).thenReturn(ResponseEntity.ok(customerResponse));

        Optional<Address> result = customerGateway.findAddress(customerId, addressId);

        assertTrue(result.isPresent());
        assertEquals(addressTO.nickName(), result.get().getName());
        assertEquals(addressTO.street(), result.get().getStreet());
        assertEquals(addressTO.city(), result.get().getCity());
        assertEquals(addressTO.state(), result.get().getState());
        assertEquals(addressTO.zipCode(), result.get().getZipCode());
        verify(customerRestClient, times(1)).find(customerId);
    }

    @Test
    void shouldReturnEmptyWhenCustomerNotFound() {
        String customerId = "123";
        when(customerRestClient.find(customerId)).thenReturn(ResponseEntity.ok(null));

        Optional<Address> result = customerGateway.findAddress(customerId, "456");

        assertTrue(result.isEmpty());
        verify(customerRestClient, times(1)).find(customerId);
    }

    @Test
    void shouldReturnEmptyWhenAddressNotFound() {
        String customerId = "123";
        String addressId = "456";
        AddressTO addressTO = new AddressTO("789", "Work", "Second St", "City", "State", "67890", 200);
        CustomerResponse customerResponse = new CustomerResponse(customerId, Set.of(addressTO));
        when(customerRestClient.find(customerId)).thenReturn(ResponseEntity.ok(customerResponse));

        Optional<Address> result = customerGateway.findAddress(customerId, addressId);

        assertTrue(result.isEmpty());
        verify(customerRestClient, times(1)).find(customerId);
    }

    @Test
    void shouldReturnEmptyWhenCustomerResponseIsNull() {
        String customerId = "123";
        when(customerRestClient.find(customerId)).thenReturn(ResponseEntity.ok(null));

        Optional<Address> result = customerGateway.findAddress(customerId, "456");

        assertTrue(result.isEmpty());
        verify(customerRestClient, times(1)).find(customerId);
    }
}