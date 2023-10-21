package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.documentnumber.DocumentNumber;

import java.time.LocalDateTime;
import java.util.UUID;

@AggregateRoot
public class Purchase {
    private final UUID buyerId;
    private final UUID orderId;
    private final LocalDateTime creationDateTime;
    private final DocumentNumber documentNumber;
    private final UUID paymentMethodId;

    private Purchase(Builder builder) {
        this.orderId = builder.orderId;
        this.buyerId = builder.buyerId;
        this.creationDateTime = builder.creationDateTime;
        this.documentNumber = builder.documentNumber;
        this.paymentMethodId = builder.paymentMethodId;
    }

    @Factory
    static class Builder {
        private UUID orderId;
        private UUID buyerId;
        private LocalDateTime creationDateTime;
        private DocumentNumber documentNumber;
        private UUID paymentMethodId;

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

        Builder paymentMethodId(UUID paymentMethodId) {
            this.paymentMethodId = paymentMethodId;
            return this;
        }
    }
}
