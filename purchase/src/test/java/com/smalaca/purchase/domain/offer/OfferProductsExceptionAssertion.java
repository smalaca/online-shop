package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OfferProductsExceptionAssertion {
    private final OfferProductsException actual;

    private OfferProductsExceptionAssertion(OfferProductsException actual) {
        this.actual = actual;
    }

    public static OfferProductsExceptionAssertion assertOfferProductsException(RuntimeException actual) {
        return new OfferProductsExceptionAssertion((OfferProductsException) actual);
    }

    public OfferProductsExceptionAssertion hasMessage(String expected) {
        assertThat(actual).hasMessage(expected);
        return this;
    }

    public OfferProductsExceptionAssertion hasProducts(int expected) {
        assertThat(actual).extracting("products").satisfies(products -> {
            assertThat((List<Product>) products).hasSize(expected);
        });

        return this;
    }

    public OfferProductsExceptionAssertion containsProduct(UUID expectedProductId, int expectedAmount) {
        assertThat(actual).extracting("products").satisfies(products -> {
            assertThat((List<Product>) products).contains(Product.product(expectedProductId, expectedAmount));
        });

        return this;
    }

    public OfferProductsExceptionAssertion hasOnlyOneProduct(UUID expectedProductId, int expectedAmount) {
        return hasProducts(1).containsProduct(expectedProductId, expectedAmount);
    }
}
