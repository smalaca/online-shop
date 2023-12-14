package com.smalaca.purchase.domain.quantity;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

@ValueObject
@EqualsAndHashCode
public class Quantity {
    private final Integer value;

    private Quantity(Integer value) {
        this.value = value;
    }

    @Factory
    public static Quantity quantity(Integer value) {
        if (value < 1) {
            throw new InvalidQuantityException(value);
        }

        return new Quantity(value);
    }

    @Factory
    public Quantity increase(Quantity quantity) {
        return new Quantity(this.value + quantity.value);
    }

    public boolean isLowerThan(Quantity quantity) {
        return this.value < quantity.value;
    }

    public boolean isLowerOrEqualThan(Quantity quantity) {
        return this.value <= quantity.value;
    }

    @Factory
    public Quantity decrease(Quantity quantity) {
        return new Quantity(this.value - quantity.value);
    }

    public Integer getValue() {
        return value;
    }
}
