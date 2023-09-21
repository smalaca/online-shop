package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.product.Product;

import java.util.ArrayList;
import java.util.List;

class OfferProductsException extends RuntimeException {
    private final List<Product> products = new ArrayList<>();

    private OfferProductsException(String message) {
        super(message);
    }

    static RuntimeException notAvailable(List<Product> products) {
        OfferProductsException exception = new OfferProductsException("Cannot create Offer because products are not available anymore.");
        exception.products.addAll(products);

        return exception;
    }
}
