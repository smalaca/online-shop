package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
public class BuyerId {
    private final UUID id;

    BuyerId(UUID id) {
        this.id = id;
    }
}
