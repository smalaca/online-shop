package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.price.Price;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Offer {
    private final LocalDateTime creationDateTime;
    private final List<OfferItem> items;
    private final UUID deliveryMethodId;
    private final Price deliveryPrice;
    private final DeliveryAddress deliveryAddress;
    private final OfferNumber offerNumber;
    private final UUID buyerId;

    private Offer(Builder builder) {
        this.creationDateTime = builder.creationDateTime;
        this.items = builder.items;
        this.deliveryMethodId = builder.deliveryMethodId;
        this.deliveryAddress = builder.deliveryAddress;
        this.deliveryPrice = builder.deliveryPrice;
        this.offerNumber = builder.offerNumber;
        this.buyerId = builder.buyerId;
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
        private UUID deliveryMethodId;
        private DeliveryAddress deliveryAddress;
        private Price deliveryPrice;
        private OfferNumber offerNumber;
        private UUID buyerId;

        Offer build() {
            offerNumber = OfferNumber.offerNumber(buyerId, creationDateTime);
            return new Offer(this);
        }

        Builder creationDateTime(LocalDateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
            return this;
        }

        Builder delivery(UUID deliveryMethod, DeliveryAddress deliveryAddress, Price deliveryPrice) {
            this.deliveryMethodId = deliveryMethod;
            this.deliveryAddress = deliveryAddress;
            this.deliveryPrice = deliveryPrice;
            return this;
        }

        Builder buyerId(UUID buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        void item(UUID sellerId, UUID productId, Amount amount, Price price) {
            items.add(new OfferItem(sellerId, productId, amount, price));
        }
    }
}
