package com.smalaca.purchase.domain.deliveryaddress;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

@ValueObject
@EqualsAndHashCode
public class DeliveryAddress {
    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;

    public DeliveryAddress(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }
}
