package com.smalaca.purchase.domain.cart;

import java.util.ArrayList;
import java.util.List;

class CartProductsException extends RuntimeException {
    private final List<Product> products = new ArrayList<>();

    private CartProductsException(String message) {
        super(message);
    }

    static RuntimeException choseNothing() {
        return new CartProductsException("Cannot create Offer when no products were choose.");
    }

    static RuntimeException missing(List<Product> products) {
        CartProductsException exception = new CartProductsException("Cannot create Offer when products are not in the Cart.");
        exception.products.addAll(products);

        return exception;
    }

    List<Product> getProducts() {
        return products;
    }
}
