package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Offer {
    private final LocalDateTime creationDateTime;
    private final List<OfferItem> items;

    private Offer(Builder builder) {
        this.creationDateTime = builder.creationDateTime;
        this.items = builder.items;
    }

    @PrimaryPort
    public void reject() {

    }

    @PrimaryPort
    @Factory
    public Offer recreate() {
        return new Offer(null);
    }

    @PrimaryPort
    @Factory
    public Order accept() {
        return new Order();
    }

    @Factory
    static class Builder {
        private final List<OfferItem> items = new ArrayList<>();
        private LocalDateTime creationDateTime;

        Offer build() {
            return new Offer(this);
        }

        Builder creationDateTime(LocalDateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
            return this;
        }

        void item(UUID productId, Amount amount) {
            items.add(new OfferItem(productId, amount));
        }
    }
}
