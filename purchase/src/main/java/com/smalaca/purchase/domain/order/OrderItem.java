package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.Entity;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.productmanagementservice.AvailableProduct;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@EqualsAndHashCode
class OrderItem {
    private final UUID sellerId;
    private final UUID productId;
    private final Quantity quantity;
    private final Price price;

    OrderItem(AvailableProduct product) {
        this.sellerId = product.getSellerId();
        this.productId = product.getProductId();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
    }
}
