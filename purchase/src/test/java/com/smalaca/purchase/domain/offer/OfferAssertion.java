package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.price.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class OfferAssertion {
    private static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

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
                    OfferItem expected = new OfferItem(
                            expectedSellerId, expectedProductId, Amount.amount(expectedAmount), Price.price(expectedPrice));
                    assertThat((List) actualItems).contains(expected);
                });
        return this;
    }

    public OfferAssertion hasOfferNumberThatStartsWith(String expected) {
        assertThat(actual).extracting("offerNumber").extracting("value")
                .satisfies(offerNumber -> {
                    String actualOfferNumber = (String) offerNumber;
                    assertThat(actualOfferNumber).matches(Pattern.compile("^" + expected + UUID_REGEX + "$"));
                });
        return this;
    }

    public OfferAssertion hasBuyerId(UUID expected) {
        assertThat(actual).extracting("buyerId").isEqualTo(expected);
        return this;
    }

    public OfferAssertion hasDelivery(UUID expectedMethodId, DeliveryAddress expectedAddress, Price expectedPrice) {
        Delivery expected = new Delivery(expectedMethodId, expectedAddress, expectedPrice);
        assertThat(actual).extracting("delivery").isEqualTo(expected);

        return this;
    }
}
