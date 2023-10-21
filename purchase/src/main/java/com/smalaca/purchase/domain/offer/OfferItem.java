package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Entity;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.product.Product;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@EqualsAndHashCode
class OfferItem {
    private final UUID sellerId;
    private final UUID productId;
    private final Quantity quantity;
    private final Price price;

    OfferItem(UUID sellerId, UUID productId, Quantity quantity, Price price) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    Product asProduct() {
        return Product.product(productId, quantity.getValue());
    }
}
