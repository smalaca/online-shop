package com.smalaca.purchase.domain.cart;

import com.smalaca.purchase.domain.selection.Selection;

import java.util.ArrayList;
import java.util.List;

class CartProductsException extends RuntimeException {
    private final List<Selection> selections = new ArrayList<>();

    private CartProductsException(String message) {
        super(message);
    }

    static RuntimeException choseNothing() {
        return new CartProductsException("Cannot create Offer when no products were choose.");
    }

    static RuntimeException missing(List<Selection> selections) {
        CartProductsException exception = new CartProductsException("Cannot create Offer when products are not in the Cart.");
        exception.selections.addAll(selections);

        return exception;
    }
}
