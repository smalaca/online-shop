package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@ValueObject
@EqualsAndHashCode
class Price {
    private final BigDecimal value;

    private Price(BigDecimal value) {
        this.value = value;
    }

    static Price price(BigDecimal value) {
        return new Price(value);
    }
}
