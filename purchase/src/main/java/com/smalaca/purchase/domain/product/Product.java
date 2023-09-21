package com.smalaca.purchase.domain.product;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
import com.smalaca.purchase.domain.amount.Amount;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ValueObject
@EqualsAndHashCode
public class Product {
    private final UUID productId;
    private final Amount amount;

    private Product(UUID productId, Amount amount) {
        this.productId = productId;
        this.amount = amount;
    }

    @Factory
    public static Product product(UUID productId, Integer amount) {
        return new Product(productId, Amount.amount(amount));
    }

    public boolean hasProductIdSameAs(UUID productId) {
        return this.productId.equals(productId);
    }

    public Amount getAmount() {
        return amount;
    }

    public UUID getProductId() {
        return productId;
    }

    public boolean hasLessThan(Amount amount) {
        return this.amount.isLowerThan(amount);
    }

    public boolean hasLessOrEqualThan(Amount amount) {
        return this.amount.isLowerOrEqualThan(amount);
    }

    public boolean isAvailabilitySatisfied(Product available) {
        return hasProductIdSameAs(available.productId) && hasLessOrEqualThan(available.amount);
    }
}
