package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.documentnumber.DocumentNumber;

import java.time.LocalDateTime;
import java.util.UUID;

@AggregateRoot
public class Purchase {
    private final UUID orderId;
    private final UUID buyerId;
    private final LocalDateTime creationDateTime;
    private final DocumentNumber documentNumber;

    private Purchase(Builder builder) {
        this.orderId = builder.orderId;
        this.buyerId = builder.buyerId;
        this.creationDateTime = builder.creationDateTime;
        this.documentNumber = builder.documentNumber;
    }

    @Factory
    static class Builder {
        private UUID orderId;
        private UUID buyerId;
        private LocalDateTime creationDateTime;
        private DocumentNumber documentNumber;

        Purchase build() {
            documentNumber = DocumentNumber.purchaseNumber(buyerId, creationDateTime);
            return new Purchase(this);
        }

        Builder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        Builder buyerId(UUID buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        Builder creationDateTime(LocalDateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
            return this;
        }
    }
}
