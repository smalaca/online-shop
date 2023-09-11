package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
public class ProductId {
    private final UUID id;

    private ProductId(UUID id) {
        this.id = id;
    }

    @Factory
    public static ProductId from(UUID id) {
        return new ProductId(id);
    }
}
