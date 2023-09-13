package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
public class OrderId {
    private final UUID id;

    private OrderId(UUID id) {
        this.id = id;
    }

    @Factory
    public static OrderId from(UUID id) {
        return new OrderId(id);
    }
}
