package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
public class OrderId {
    private final UUID id;

    public OrderId(UUID id) {
        this.id = id;
    }
}
