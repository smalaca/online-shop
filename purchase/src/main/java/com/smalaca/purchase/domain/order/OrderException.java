package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.selection.Selection;

import java.util.ArrayList;
import java.util.List;

class OrderException extends RuntimeException {
    private final List<Selection> selections = new ArrayList<>();

    private OrderException(String message) {
        super(message);
    }

    static RuntimeException notAvailableProducts(List<Selection> selections) {
        OrderException exception = new OrderException("Cannot create Order because products are not available anymore.");
        exception.selections.addAll(selections);

        return exception;
    }
}
