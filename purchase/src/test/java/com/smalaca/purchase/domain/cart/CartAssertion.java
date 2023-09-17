package com.smalaca.purchase.domain.cart;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CartAssertion {
    private final Cart actual;

    private CartAssertion(Cart actual) {
        this.actual = actual;
    }

    public static CartAssertion assertCart(Cart actual) {
        return new CartAssertion(actual);
    }

    public CartAssertion hasOnlyProduct(UUID expectedProductId, int expectedAmount) {
        return hasProducts(1).hasProduct(expectedProductId, expectedAmount);
    }

    public CartAssertion hasProducts(int expected) {
        assertThat(actual).extracting("items").satisfies(actualItems -> {
            assertThat((List) actualItems).hasSize(expected);
        });

        return this;
    }

    public CartAssertion hasProduct(UUID expectedProductId, int expectedAmount) {
        assertThat(actual).extracting("items")
                .satisfies(actualItems -> {
                    assertThat((List) actualItems)
                            .contains(new CartItem(expectedProductId, Amount.amount(expectedAmount)));
                });
        return this;
    }
}