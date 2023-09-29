package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.product.Product;

import java.util.ArrayList;
import java.util.List;

class OrderException extends RuntimeException {
    private final List<Product> products = new ArrayList<>();

    private OrderException(String message) {
        super(message);
    }

    static RuntimeException notAvailableProducts(List<Product> products) {
        OrderException exception = new OrderException("Cannot create Order because products are not available anymore.");
        exception.products.addAll(products);

        return exception;
    }
}
