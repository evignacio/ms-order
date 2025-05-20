package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.gateway.CustomerGateway;
import com.fiap.order.infrastructure.integration.rest.CustomerRestClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomerGatewayImpl implements CustomerGateway {
    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found, id {}";

    private final CustomerRestClient customerRestClient;

    public CustomerGatewayImpl(CustomerRestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }

    @Override
    public Optional<Address> findAddress(String customerId, String addressId) {
        try {
            log.info("Fetching address with ID {} for customer {}", addressId, customerId);
            var customerResponse = customerRestClient.find(customerId).getBody();
            if (customerResponse == null) {
                log.warn(CUSTOMER_NOT_FOUND_MESSAGE, customerId);
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
        } catch (FeignException exception) {
            if (exception.status() == HttpStatus.NO_CONTENT.value() || exception.status() == HttpStatus.NOT_FOUND.value()) {
                log.warn(CUSTOMER_NOT_FOUND_MESSAGE, customerId);
                return Optional.empty();
            }
            log.error("Error fetching customer {}", customerId, exception);
            throw exception;
        }
    }
}
