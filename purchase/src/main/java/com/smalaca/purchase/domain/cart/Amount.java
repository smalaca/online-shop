package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

@ValueObject
@EqualsAndHashCode
class Amount {
    private final Integer value;

    private Amount(Integer value) {
        this.value = value;
    }

    @Factory
    static Amount amount(Integer value) {
        if (value < 1) {
            throw new InvalidAmountException(value);
        }

        return new Amount(value);
    }

    @Factory
    Amount increase(Amount amount) {
        return new Amount(this.value + amount.value);
    }

    boolean isLowerThan(Amount amount) {
        return this.value < amount.value;
    }

    boolean isLowerOrEqualThan(Amount amount) {
        return this.value <= amount.value;
    }

    @Factory
    Amount decrease(Amount amount) {
        return new Amount(this.value - amount.value);
    }
}
