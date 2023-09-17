package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ValueObject
@EqualsAndHashCode
public class CartId {
    private final UUID id;

    public CartId(UUID id) {
        this.id = id;
    }
}
