package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
public class CartId {
    private final UUID id;

    private CartId(UUID id) {
        this.id = id;
    }

    @Factory
    public static CartId from(UUID id) {
        return new CartId(id);
    }
}
