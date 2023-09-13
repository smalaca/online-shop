package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.purchase.domain.productid.ProductId;

import java.util.List;

@AggregateRoot
public class Offer {
    private final List<ProductId> productsIds;

    public Offer(List<ProductId> productsIds) {
        this.productsIds = productsIds;
    }
}