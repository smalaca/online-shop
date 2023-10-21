package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Entity;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.product.Product;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@EqualsAndHashCode
class CartItem {
    private final UUID productId;
    private Quantity quantity;

    CartItem(UUID productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    boolean isFor(Product product) {
        return product.hasProductIdSameAs(productId);
    }

    boolean isEnoughOf(Product product) {
        return isFor(product) && product.hasLessOrEqualThan(quantity);
    }

    void increase(Quantity quantity) {
        this.quantity = this.quantity.increase(quantity);
    }

    boolean hasMoreThan(Product product) {
        return product.hasLessThan(quantity);
    }

    void decrease(Quantity quantity) {
        this.quantity = this.quantity.decrease(quantity);
    }
}
