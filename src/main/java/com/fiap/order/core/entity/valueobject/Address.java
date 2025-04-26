package com.fiap.order.core.entity.valueobject;

public class Address {
    private String name;
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    private Address(String name, String street, String number, String city, String state, String country, String zipCode) {
        setName(name);
        setStreet(street);
        setNumber(number);
        setCity(city);
        setState(state);
        setCountry(country);
        setZipCode(zipCode);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        if (name != null  && name.length() < 3)
            throw new IllegalArgumentException("Name cannot be less than 3 characters");

        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    private void setStreet(String street) {
        if (street == null || street.length() < 3)
            throw new IllegalArgumentException("Street cannot be null or less than 3 characters");

        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    private void setNumber(String number) {
        if (number != null && number.length() < 1)
            throw new IllegalArgumentException("Number cannot be less than 1 character");

        this.number = number;
    }

    public String getCity() {
        return city;
    }

    private void setCity(String city) {
        if (city == null || city.length() < 3)
            throw new IllegalArgumentException("City cannot be null or less than 3 characters");

        this.city = city;
    }

    public String getState() {
        return state;
    }

    private void setState(String state) {
        if (state == null || state.length() < 2)
            throw new IllegalArgumentException("State cannot be null or less than 2 characters");

        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    private void setCountry(String country) {
        if (country == null || country.length() < 3)
            throw new IllegalArgumentException("Country cannot be null or less than 3 characters");

        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    private void setZipCode(String zipCode) {
        if (zipCode == null || zipCode.length() < 5)
            throw new IllegalArgumentException("Zip code cannot be null or less than 5 characters");

        this.zipCode = zipCode;
    }

    public static class AddressBuilder {
        private String name;
        private String street;
        private String number;
        private String city;
        private String state;
        private String country;
        private String zipCode;

        public static AddressBuilder builder() {
            return new AddressBuilder();
        }

        public AddressBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AddressBuilder number(String number) {
            this.number = number;
            return this;
        }

        public Address build(String street, String city, String state, String country, String zipCode) {
            return new Address(name, street, number, city, state, country, zipCode);
        }
    }
}
