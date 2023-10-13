package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.ddd.AggregateRoot;

import java.util.UUID;

@AggregateRoot
public class Purchase {
    private final UUID orderId;

    public Purchase(UUID orderId) {
        this.orderId = orderId;
    }
}
