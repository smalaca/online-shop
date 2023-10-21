package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;

import java.util.ArrayList;
import java.util.List;

class OrderException extends RuntimeException {
    private final List<QuantitativeProduct> quantitativeProducts = new ArrayList<>();

    private OrderException(String message) {
        super(message);
    }

    static RuntimeException notAvailableProducts(List<QuantitativeProduct> quantitativeProducts) {
        OrderException exception = new OrderException("Cannot create Order because products are not available anymore.");
        exception.quantitativeProducts.addAll(quantitativeProducts);

        return exception;
    }
}
