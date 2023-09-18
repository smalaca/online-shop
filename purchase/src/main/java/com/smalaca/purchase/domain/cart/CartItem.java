package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Entity;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@EqualsAndHashCode
class CartItem {
    private final UUID productId;
    private Amount amount;

    CartItem(UUID productId, Amount amount) {
        this.productId = productId;
        this.amount = amount;
    }

    boolean isFor(Product product) {
        return product.hasProductIdSameAs(productId);
    }

    boolean isEnoughOf(Product product) {
        return isFor(product) && product.hasLessOrEqualThan(amount);
    }

    void increase(Amount amount) {
        this.amount = this.amount.increase(amount);
    }

    boolean hasMoreThan(Product product) {
        return product.hasLessThan(amount);
    }

    void decrease(Amount amount) {
        this.amount = this.amount.decrease(amount);
    }
}
