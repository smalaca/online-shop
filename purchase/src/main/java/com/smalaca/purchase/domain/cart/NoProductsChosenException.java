package com.smalaca.purchase.domain.cart;

class NoProductsChosenException extends RuntimeException {
    NoProductsChosenException() {
        super("Cannot create Offer when no products were choose.");
    }
}
