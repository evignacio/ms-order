package com.fiap.order.infrastructure.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddressModel {
    private String name;
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;
    private String zipCode;

}
