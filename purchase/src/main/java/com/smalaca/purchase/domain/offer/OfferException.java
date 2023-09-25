package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.deliveryaddress.AddressDto;
import com.smalaca.purchase.domain.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class OfferException extends RuntimeException {
    private final List<Product> products = new ArrayList<>();

    private OfferException(String message) {
        super(message);
    }

    static RuntimeException notAvailableProducts(List<Product> products) {
        OfferException exception = new OfferException("Cannot create Offer because products are not available anymore.");
        exception.products.addAll(products);

        return exception;
    }

    static RuntimeException unsupportedDeliveryMethod(UUID deliveryMethodId) {
        return new OfferException("Delivery Method: " + deliveryMethodId + " is not supported.");
    }

    static RuntimeException notExistingAddress(AddressDto addressDto) {
        return new OfferException("Address: " + addressDto + " do not exist.");
    }
}
