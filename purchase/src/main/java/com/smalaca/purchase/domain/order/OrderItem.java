package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.Entity;
import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.price.Price;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@EqualsAndHashCode
class OrderItem {
    private final UUID sellerId;
    private final UUID productId;
    private final Amount amount;
    private final Price price;

    OrderItem(UUID sellerId, UUID productId, Amount amount, Price price) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.amount = amount;
        this.price = price;
    }
}
