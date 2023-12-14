package com.smalaca.purchase.domain.cart;

import com.smalaca.purchase.domain.selection.Selection;
import com.smalaca.purchase.domain.selection.SelectionTestFactory;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CartProductsExceptionAssertion {
    private final CartProductsException actual;

    private CartProductsExceptionAssertion(CartProductsException actual) {
        this.actual = actual;
    }

    public static CartProductsExceptionAssertion assertCartProductsException(RuntimeException actual) {
        return new CartProductsExceptionAssertion((CartProductsException) actual);
    }

    public CartProductsExceptionAssertion hasMessage(String expected) {
        assertThat(actual).hasMessage(expected);
        return this;
    }

    public CartProductsExceptionAssertion hasProducts(int expected) {
        assertThat(actual).extracting("selections").satisfies(products -> {
            assertThat((List<Selection>) products).hasSize(expected);
        });

        return this;
    }

    public CartProductsExceptionAssertion containsProduct(UUID expectedProductId, int expectedQuantity) {
        assertThat(actual).extracting("selections").satisfies(products -> {
            assertThat((List<Selection>) products).contains(SelectionTestFactory.selection(expectedProductId, expectedQuantity));
        });

        return this;
    }

    public CartProductsExceptionAssertion hasOnlyOneProduct(UUID expectedProductId, int expectedQuantity) {
        return hasProducts(1).containsProduct(expectedProductId, expectedQuantity);
    }
}
