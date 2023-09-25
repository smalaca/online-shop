package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.amount.Amount;

import java.math.BigDecimal;
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

    public OfferAssertion containsProduct(UUID expectedSellerId, UUID expectedProductId, int expectedAmount, BigDecimal expectedPrice) {
        assertThat(actual).extracting("items")
                .satisfies(actualItems -> {
                    assertThat((List) actualItems)
                            .contains(new OfferItem(expectedSellerId, expectedProductId, Amount.amount(expectedAmount), Price.price(expectedPrice)));
                });
        return this;
    }

    public OfferAssertion hasDeliveryMethod(UUID expected) {
        assertThat(actual).extracting("deliveryMethodId").isEqualTo(expected);
        return this;
    }

    public OfferAssertion hasDeliveryPrice(Price expected) {
        assertThat(actual).extracting("deliveryPrice").isEqualTo(expected);
        return this;
    }
}
