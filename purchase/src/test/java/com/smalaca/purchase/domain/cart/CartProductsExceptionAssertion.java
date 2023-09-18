package com.smalaca.purchase.domain.cart;

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
        assertThat(actual.getProducts()).hasSize(expected);
        return this;
    }

    public CartProductsExceptionAssertion containsProduct(UUID expectedProductId, int expectedAmount) {
        assertThat(actual.getProducts()).contains(Product.product(expectedProductId, expectedAmount));
        return this;
    }

    public CartProductsExceptionAssertion hasOnlyOneProduct(UUID expectedProductId, int expectedAmount) {
        return hasProducts(1).containsProduct(expectedProductId, expectedAmount);
    }
}
