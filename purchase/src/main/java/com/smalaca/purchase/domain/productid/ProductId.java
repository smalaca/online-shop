package com.smalaca.purchase.domain.productid;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ValueObject
@EqualsAndHashCode
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
