package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
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
}
