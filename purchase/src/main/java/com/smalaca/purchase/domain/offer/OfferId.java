package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
public class OfferId {
    private final UUID id;

    private OfferId(UUID id) {
        this.id = id;
    }

    @Factory
    public static OfferId from(UUID id) {
        return new OfferId(id);
    }

}
