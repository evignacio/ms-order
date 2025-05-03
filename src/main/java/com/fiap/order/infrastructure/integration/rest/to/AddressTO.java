package com.fiap.order.infrastructure.integration.rest.to;

public record AddressTO(String id, String nickName, String street, String city, String state, String zipCode,
                        Integer number) {
}
