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
        return create(products, "Cannot create Offer when products are not in the Cart.");
    }

    static RuntimeException notAvailable(List<Product> products) {
        return create(products, "Cannot create Offer because products are not available anymore.");
    }

    private static CartProductsException create(List<Product> products, String message) {
        CartProductsException exception = new CartProductsException(message);
        exception.products.addAll(products);

        return exception;
    }

    List<Product> getProducts() {
        return products;
    }
}
