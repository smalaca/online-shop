package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.product.Product;

import java.util.UUID;

public class AvailableProduct {
    private final UUID productId;
    private final Amount amount;

    private AvailableProduct(UUID productId, Amount amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public static AvailableProduct product(UUID productId, Integer amount) {
        return new AvailableProduct(productId, Amount.amount(amount));
    }

    boolean isAvailableFor(Product product) {
        return product.hasProductIdSameAs(productId) && product.hasLessOrEqualThan(amount);
    }
}
