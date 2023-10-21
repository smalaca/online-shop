package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.documentnumber.DocumentNumber;
import com.smalaca.purchase.domain.productmanagementservice.AvailableProduct;
import com.smalaca.purchase.domain.purchase.Purchase;
import com.smalaca.purchase.domain.purchase.PurchaseFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
public class Order {
    private UUID orderId;
    private final DocumentNumber documentNumber;
    private final UUID offerId;
    private final UUID buyerId;
    private final Delivery delivery;
    private final LocalDateTime creationDateTime;
    private final List<OrderItem> items;

    private Order(Builder builder) {
        this.documentNumber = builder.documentNumber;
        this.offerId = builder.offerId;
        this.buyerId = builder.buyerId;
        this.delivery = builder.delivery;
        this.creationDateTime = builder.creationDateTime;
        this.items = builder.items;
    }

    @PrimaryPort
    @Factory
    public Purchase purchase(UUID paymentMethodId, PurchaseFactory purchaseFactory) {
        return purchaseFactory.create(buyerId, orderId, paymentMethodId);
    }

    @Factory
    static class Builder {
        private DocumentNumber documentNumber;
        private UUID offerId;
        private UUID buyerId;
        private Delivery delivery;
        private LocalDateTime creationDateTime;
        private final List<OrderItem> items = new ArrayList<>();

        Order build() {
            documentNumber = DocumentNumber.orderNumber(buyerId, creationDateTime);
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

        Builder creationDateTime(LocalDateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
            return this;
        }

        void item(AvailableProduct product) {
            items.add(new OrderItem(product));
        }
    }
}
