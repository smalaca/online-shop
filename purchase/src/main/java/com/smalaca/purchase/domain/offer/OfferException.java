package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.product.Product;

import java.util.ArrayList;
import java.util.List;

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

    static RuntimeException unsupportedDelivery(String deliveryMethod) {
        return new OfferException("Delivery Method: " + deliveryMethod + " is not supported.");
    }
}
