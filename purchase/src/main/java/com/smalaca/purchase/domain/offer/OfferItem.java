package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Entity;
import com.smalaca.purchase.domain.amount.Amount;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@EqualsAndHashCode
class OfferItem {
    private final UUID sellerId;
    private final UUID productId;
    private final Amount amount;
    private final Price price;

    OfferItem(UUID sellerId, UUID productId, Amount amount, Price price) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.amount = amount;
        this.price = price;
    }
}
