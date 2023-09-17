package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

@ValueObject
@EqualsAndHashCode
class Amount {
    private final Integer value;

    private Amount(Integer value) {
        this.value = value;
    }

    static Amount amount(Integer value) {
        if (value < 1) {
            throw new InvalidAmountException(value);
        }

        return new Amount(value);
    }

    Amount increase(Amount amount) {
        return new Amount(this.value + amount.value);
    }
}
