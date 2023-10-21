package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

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
        assertThat(actual).extracting("products").satisfies(products -> {
            assertThat((List<Product>) products).hasSize(expected);
        });

        return this;
    }

    public OrderExceptionAssertion containsProduct(UUID expectedProductId, int expectedQuantity) {
        assertThat(actual).extracting("products").satisfies(products -> {
            assertThat((List<Product>) products).contains(Product.product(expectedProductId, expectedQuantity));
        });

        return this;
    }

    public OrderExceptionAssertion hasOnlyOneProduct(UUID expectedProductId, int expectedQuantity) {
        return hasProducts(1).containsProduct(expectedProductId, expectedQuantity);
    }
}
