package com.smalaca.purchase.domain.amount;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

@ValueObject
@EqualsAndHashCode
public class Amount {
    private final Integer value;

    private Amount(Integer value) {
        this.value = value;
    }

    @Factory
    public static Amount amount(Integer value) {
        if (value < 1) {
            throw new InvalidAmountException(value);
        }

        return new Amount(value);
    }

    @Factory
    public Amount increase(Amount amount) {
        return new Amount(this.value + amount.value);
    }

    public boolean isLowerThan(Amount amount) {
        return this.value < amount.value;
    }

    public boolean isLowerOrEqualThan(Amount amount) {
        return this.value <= amount.value;
    }

    @Factory
    public Amount decrease(Amount amount) {
        return new Amount(this.value - amount.value);
    }
}
