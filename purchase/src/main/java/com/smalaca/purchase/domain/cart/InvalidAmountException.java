package com.smalaca.purchase.domain.cart;

class InvalidAmountException extends RuntimeException {
    InvalidAmountException(Integer amount) {
        super("Amount: \"" + amount + "\" is not greater than zero.");
    }
}
