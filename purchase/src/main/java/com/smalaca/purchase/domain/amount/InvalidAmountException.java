package com.smalaca.purchase.domain.amount;

class InvalidAmountException extends RuntimeException {
    InvalidAmountException(Integer amount) {
        super("Amount: \"" + amount + "\" is not greater than zero.");
    }
}
