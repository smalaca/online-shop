package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.clock.Clock;

import java.util.UUID;

@Factory
public class PurchaseFactory {
    private final Clock clock;

    public PurchaseFactory(Clock clock) {
        this.clock = clock;
    }

    public Purchase create(UUID buyerId, UUID orderId) {
        return new Purchase.Builder()
                .buyerId(buyerId)
                .orderId(orderId)
                .creationDateTime(clock.nowDateTime())
                .build();
    }
}
