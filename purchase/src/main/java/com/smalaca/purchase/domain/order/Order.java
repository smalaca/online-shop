package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.purchase.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Order {
    private final UUID offerId;
    private final Delivery delivery;
    private final List<OrderItem> items;

    private Order(Builder builder) {
        this.offerId = builder.offerId;
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
    public static class Builder {
        private UUID offerId;
        private Delivery delivery;
        private final List<OrderItem> items = new ArrayList<>();

        public Order build() {
            return new Order(this);
        }

        public Builder offerId(UUID offerId) {
            this.offerId = offerId;
            return this;
        }

        public Builder delivery(Delivery delivery) {
            this.delivery = delivery;
            return this;
        }

        public void item(UUID sellerId, UUID productId, Amount amount, Price price) {
            items.add(new OrderItem(sellerId, productId, amount, price));
        }
    }
}
