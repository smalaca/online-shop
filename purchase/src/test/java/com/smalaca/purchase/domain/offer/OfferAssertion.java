package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.amount.Amount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    public OfferAssertion hasProducts(int expected) {
        assertThat(actual).extracting("items").satisfies(actualItems -> {
            assertThat((List) actualItems).hasSize(expected);
        });

        return this;
    }

    public OfferAssertion containsProduct(UUID expectedProductId, int expectedAmount) {
        assertThat(actual).extracting("items")
                .satisfies(actualItems -> {
                    assertThat((List) actualItems)
                            .contains(new OfferItem(expectedProductId, Amount.amount(expectedAmount)));
                });
        return this;
    }
}
