package com.fiap.order.infrastructure.gateway;

import com.fiap.order.core.entity.valueobject.Address;
import com.fiap.order.core.gateway.CustomerGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomerGatewayImpl implements CustomerGateway {

    @Override
    public Optional<Address> findAddress(String customerId, String addressId) {
        return Optional.empty();
    }
}
