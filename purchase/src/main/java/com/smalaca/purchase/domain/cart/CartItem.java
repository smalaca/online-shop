package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Entity;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.selection.Selection;
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

    boolean isFor(Selection selection) {
        return selection.hasProductIdSameAs(productId);
    }

    boolean isEnoughOf(Product product) {
        return isFor(product.asSelection()) && product.hasLessOrEqualThan(quantity);
    }

    void increase(Quantity quantity) {
        this.quantity = this.quantity.increase(quantity);
    }

    boolean hasMoreThan(Selection selection) {
        return selection.hasLessThan(quantity);
    }

    void decrease(Quantity quantity) {
        this.quantity = this.quantity.decrease(quantity);
    }
}
