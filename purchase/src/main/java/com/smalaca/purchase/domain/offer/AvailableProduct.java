package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.product.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class AvailableProduct {
    private final UUID productId;
    private final Amount amount;
    private final Price price;

    private AvailableProduct(UUID productId, Amount amount, Price price) {
        this.productId = productId;
        this.amount = amount;
        this.price = price;
    }

    public static AvailableProduct availableProduct(UUID productId, Integer amount, BigDecimal price) {
        return new AvailableProduct(productId, Amount.amount(amount), Price.price(price));
    }

    boolean isAvailableFor(Product product) {
        return product.hasProductIdSameAs(productId) && product.hasLessOrEqualThan(amount);
    }

    boolean isFor(UUID productId) {
        return this.productId == productId;
    }

    Price getPrice() {
        return price;
    }
}
