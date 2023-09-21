package com.smalaca.purchase.domain.offer;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class OfferAssertion {
    private final Offer actual;

    private OfferAssertion(Offer actual) {
        this.actual = actual;
    }

    public static OfferAssertion assertOffer(Offer actual) {
        return new OfferAssertion(actual);
    }

    public OfferAssertion hasCreationDateTime(LocalDateTime expected) {
        assertThat(actual).extracting("creationDateTime").isEqualTo(expected);
        return this;
    }
}
