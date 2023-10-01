package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.productmanagementservice.AvailableProduct;
import com.smalaca.purchase.domain.purchase.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Order {
    private final UUID offerId;
    private final UUID buyerId;
    private final Delivery delivery;
    private final List<OrderItem> items;

    private Order(Builder builder) {
        this.offerId = builder.offerId;
        this.buyerId = builder.buyerId;
        this.delivery = builder.delivery;
        this.items = builder.items;
    }

    @PrimaryPort
    public void reject() {

    }

    @PrimaryPort
    @Factory
    public Purchase purchase() {
        return new Purchase();
    }

    @Factory
    static class Builder {
        private UUID offerId;
        private UUID buyerId;
        private Delivery delivery;
        private final List<OrderItem> items = new ArrayList<>();

        Order build() {
            return new Order(this);
        }

        Builder offerId(UUID offerId) {
            this.offerId = offerId;
            return this;
        }

        Builder buyerId(UUID buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        Builder delivery(Delivery delivery) {
            this.delivery = delivery;
            return this;
        }

        void item(AvailableProduct product) {
            items.add(new OrderItem(product));
        }
    }
}
