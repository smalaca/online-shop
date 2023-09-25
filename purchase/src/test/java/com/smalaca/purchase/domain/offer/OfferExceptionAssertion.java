package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OfferExceptionAssertion {
    private final OfferException actual;

    private OfferExceptionAssertion(OfferException actual) {
        this.actual = actual;
    }

    public static OfferExceptionAssertion assertOfferProductsException(RuntimeException actual) {
        return new OfferExceptionAssertion((OfferException) actual);
    }

    public OfferExceptionAssertion hasMessage(String expected) {
        assertThat(actual).hasMessage(expected);
        return this;
    }

    public OfferExceptionAssertion hasProducts(int expected) {
        assertThat(actual).extracting("products").satisfies(products -> {
            assertThat((List<Product>) products).hasSize(expected);
        });

        return this;
    }

    public OfferExceptionAssertion containsProduct(UUID expectedProductId, int expectedAmount) {
        assertThat(actual).extracting("products").satisfies(products -> {
            assertThat((List<Product>) products).contains(Product.product(expectedProductId, expectedAmount));
        });

        return this;
    }

    public OfferExceptionAssertion hasOnlyOneProduct(UUID expectedProductId, int expectedAmount) {
        return hasProducts(1).containsProduct(expectedProductId, expectedAmount);
    }

    public OfferExceptionAssertion hasDeliveryAddress(DeliveryAddress expected) {
        assertThat(actual).extracting("deliveryAddress").isEqualTo(expected);
        return this;
    }
}
