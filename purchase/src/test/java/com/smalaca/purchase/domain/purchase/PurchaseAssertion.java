package com.smalaca.purchase.domain.purchase;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseAssertion {
    private final Purchase actual;

    private PurchaseAssertion(Purchase actual) {
        this.actual = actual;
    }

    public static PurchaseAssertion assertPurchase(Purchase actual) {
        return new PurchaseAssertion(actual);
    }

    public PurchaseAssertion hasOrderId(UUID expected) {
        assertThat(actual).extracting("orderId").isEqualTo(expected);
        return this;
    }
}
