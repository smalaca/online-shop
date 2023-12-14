package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProductTestFactory.quantitativeProduct;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderExceptionAssertion {
    private final OrderException actual;

    private OrderExceptionAssertion(OrderException actual) {
        this.actual = actual;
    }

    public static OrderExceptionAssertion assertOrderProductsException(RuntimeException actual) {
        return new OrderExceptionAssertion((OrderException) actual);
    }

    public OrderExceptionAssertion hasMessage(String expected) {
        assertThat(actual).hasMessage(expected);
        return this;
    }

    public OrderExceptionAssertion hasProducts(int expected) {
        assertThat(actual).extracting("quantitativeProducts").satisfies(products -> {
            assertThat((List<QuantitativeProduct>) products).hasSize(expected);
        });

        return this;
    }

    public OrderExceptionAssertion containsProduct(UUID expectedSellerId, UUID expectedProductId, int expectedQuantity, BigDecimal expectedPrice) {
        assertThat(actual).extracting("quantitativeProducts").satisfies(products -> {
            assertThat((List< QuantitativeProduct>) products).contains(quantitativeProduct(expectedSellerId, expectedProductId, expectedQuantity, expectedPrice));
        });

        return this;
    }
}
