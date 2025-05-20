package com.fiap.order.core.entity.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class AddressTest {

    @Test
    void shouldCreateAddress() {
        var address = Address.AddressBuilder.builder()
                .name("Home")
                .number("123")
                .build("Rua 1", "São Paulo", "SP", "Brazil", "01234567");

        assertThat(address.getName()).isEqualTo("Home");
        assertThat(address.getStreet()).isEqualTo("Rua 1");
        assertThat(address.getNumber()).isEqualTo("123");
        assertThat(address.getCity()).isEqualTo("São Paulo");
        assertThat(address.getState()).isEqualTo("SP");
        assertThat(address.getCountry()).isEqualTo("Brazil");
        assertThat(address.getZipCode()).isEqualTo("01234567");
    }

    @Test
    void shouldReturnNameNullOrInvalid() {
        var exception = catchThrowable(
                () -> Address.AddressBuilder.builder()
                        .name("")
                        .number("123")
                        .build("Rua 1", "São Paulo", "SP", "Brazil", "01234567")
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be less than 3 characters");
    }

    @Test
    void shouldReturnStreetNullOrInvalid() {
        var exception = catchThrowable(
                () -> Address.AddressBuilder.builder()
                        .name("Home")
                        .number("123")
                        .build(null, "São Paulo", "SP", "Brazil", "01234567")
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Street cannot be null or less than 3 characters");
    }

    @Test
    void shouldReturnNumberNullOrInvalid() {
        var exception = catchThrowable(
                () -> Address.AddressBuilder.builder()
                        .name("Home")
                        .number("")
                        .build("Rua 1", "São Paulo", "SP", "Brazil", "01234567")
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Number cannot be less than 1 character");
    }

    @Test
    void shouldReturnCityNullOrInvalid() {
        var exception = catchThrowable(
                () -> Address.AddressBuilder.builder()
                        .name("Home")
                        .number("123")
                        .build("Rua 1", null, "SP", "Brazil", "01234567")
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("City cannot be null or less than 3 characters");
    }

    @Test
    void shouldReturnStateNullOrInvalid() {
        var exception = catchThrowable(
                () -> Address.AddressBuilder.builder()
                        .name("Home")
                        .number("123")
                        .build("Rua 1", "São Paulo", null, "Brazil", "01234567")
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("State cannot be null or less than 2 characters");
    }

    @Test
    void shouldReturnCountryNullOrInvalid() {
        var exception = catchThrowable(
                () -> Address.AddressBuilder.builder()
                        .name("Home")
                        .number("123")
                        .build("Rua 1", "São Paulo", "SP", null, "01234567")
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country cannot be null or less than 3 characters");
    }

    @Test
    void shouldReturnZipCodeNullOrInvalid() {
        var exception = catchThrowable(
                () -> Address.AddressBuilder.builder()
                        .name("Home")
                        .number("123")
                        .build("Rua 1", "São Paulo", "SP", "Brazil", null)
        );

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Zip code cannot be null or less than 5 characters");
    }
}