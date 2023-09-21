package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
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

    @Factory
    CartItem asCartItem() {
        return new CartItem(productId, amount);
    }

    boolean hasProductIdSameAs(UUID productId) {
        return this.productId.equals(productId);
    }

    Amount getAmount() {
        return amount;
    }

    UUID getProductId() {
        return productId;
    }

    boolean hasLessThan(Amount amount) {
        return this.amount.isLowerThan(amount);
    }

    boolean hasLessOrEqualThan(Amount amount) {
        return this.amount.isLowerOrEqualThan(amount);
    }

    boolean isAvailabilitySatisfied(Product available) {
        return hasProductIdSameAs(available.productId) && hasLessOrEqualThan(available.amount);
    }
}
