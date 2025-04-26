package com.fiap.order.core.gateway;

import com.fiap.order.core.entity.valueobject.Address;

import java.util.Optional;

public interface CustomerGateway {
    Optional<Address> findAddress(String customerId, String addressId);
}
