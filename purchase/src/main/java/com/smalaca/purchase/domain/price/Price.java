package com.smalaca.purchase.domain.price;

import com.smalaca.annotations.ddd.ValueObject;
import com.smalaca.purchase.domain.quantity.Quantity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@ValueObject
@EqualsAndHashCode
@ToString
public class Price {
    public static final Price ZERO = new Price(BigDecimal.ZERO);
    private final BigDecimal value;

    private Price(BigDecimal value) {
        this.value = value;
    }

    public static Price price(BigDecimal value) {
        return new Price(value);
    }

    public Price multiply(Quantity quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity.getValue())));
    }

    public Price add(Price element) {
        return new Price(value.add(element.value));
    }
}
