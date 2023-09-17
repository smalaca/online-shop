package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.ValueObject;

import java.util.UUID;

@ValueObject
public class OfferId {
    private final UUID id;

    public OfferId(UUID id) {
        this.id = id;
    }
}
