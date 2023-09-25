package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;

public record AddressDto(String street, String city, String postalCode, String country) {
    DeliveryAddress asDeliveryAddress() {
        return new DeliveryAddress(street, city, postalCode, country);
    }
}
