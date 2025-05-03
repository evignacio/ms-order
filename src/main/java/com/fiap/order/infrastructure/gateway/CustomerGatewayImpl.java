package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.gateway.CustomerGateway;
import com.fiap.order.infrastructure.integration.rest.CustomerRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomerGatewayImpl implements CustomerGateway {

    private final CustomerRestClient customerRestClient;

    public CustomerGatewayImpl(CustomerRestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }

    @Override
    public Optional<Address> findAddress(String customerId, String addressId) {
        log.info("Fetching address with ID {} for customer {}", addressId, customerId);
        var customerResponse = customerRestClient.find(customerId).getBody();
        if (customerResponse == null) {
            log.warn("Customer not found, id {}", customerId);
            return Optional.empty();
        }
        return customerResponse.addresses()
                .stream()
                .filter(address -> address.id().equals(addressId))
                .map(address -> Address.AddressBuilder.builder()
                        .number(address.number().toString())
                        .name(address.nickName())
                        .build(address.street(), address.city(), address.state(), "Brazil", address.zipCode()))
                .findFirst();
    }
}
