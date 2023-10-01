package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.productmanagementservice.AvailableProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.smalaca.purchase.domain.productmanagementservice.AvailableProduct.availableProduct;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAssertion {
    private static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    private final Order actual;

    private OrderAssertion(Order actual) {
        this.actual = actual;
    }

    public static OrderAssertion assertOrder(Order actual) {
        return new OrderAssertion(actual);
    }

    public OrderAssertion hasProducts(int expected) {
        assertThat(actual).extracting("items").satisfies(actualItems -> {
            assertThat((List) actualItems).hasSize(expected);
        });

        return this;
    }

    public OrderAssertion containsProduct(UUID expectedSellerId, UUID expectedProductId, int expectedAmount, BigDecimal expectedPrice) {
        assertThat(actual).extracting("items")
                .satisfies(actualItems -> {
                    AvailableProduct availableProduct = availableProduct(
                            expectedSellerId, expectedProductId, expectedAmount, expectedPrice);
                    OrderItem expected = new OrderItem(availableProduct);
                    assertThat((List) actualItems).contains(expected);
                });
        return this;
    }

    public OrderAssertion hasOfferId(UUID expected) {
        assertThat(actual).extracting("offerId").isEqualTo(expected);
        return this;
    }

    public OrderAssertion hasDelivery(UUID expectedMethodId, DeliveryAddress expectedAddress, Price expectedPrice) {
        Delivery expected = new Delivery(expectedMethodId, expectedAddress, expectedPrice);
        assertThat(actual).extracting("delivery").isEqualTo(expected);

        return this;
    }

    public OrderAssertion hasBuyerId(UUID expected) {
        assertThat(actual).extracting("buyerId").isEqualTo(expected);
        return this;
    }
}
