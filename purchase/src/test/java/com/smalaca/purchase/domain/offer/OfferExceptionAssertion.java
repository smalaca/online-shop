package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.selection.Selection;
import com.smalaca.purchase.domain.selection.SelectionTestFactory;

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
        assertThat(actual).extracting("selections").satisfies(products -> {
            assertThat((List<Selection>) products).hasSize(expected);
        });

        return this;
    }

    public OfferExceptionAssertion containsProduct(UUID expectedProductId, int expectedQuantity) {
        assertThat(actual).extracting("selections").satisfies(products -> {
            assertThat((List<Selection>) products).contains(SelectionTestFactory.selection(expectedProductId, expectedQuantity));
        });

        return this;
    }

    public OfferExceptionAssertion hasOnlyOneProduct(UUID expectedProductId, int expectedQuantity) {
        return hasProducts(1).containsProduct(expectedProductId, expectedQuantity);
    }

    public OfferExceptionAssertion hasDeliveryAddress(DeliveryAddress expected) {
        assertThat(actual).extracting("deliveryAddress").isEqualTo(expected);
        return this;
    }
}
