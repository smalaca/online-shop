package com.smalaca.purchase.domain.quantity;

class InvalidQuantityException extends RuntimeException {
    InvalidQuantityException(Integer quantity) {
        super("Quantity: \"" + quantity + "\" is not greater than zero.");
    }
}
