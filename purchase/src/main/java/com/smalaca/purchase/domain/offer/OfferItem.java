package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Entity;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.selection.Selection;
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

    Selection asSelection() {
        return Selection.selection(productId, quantity.getValue());
    }
}
