package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.purchase.domain.productid.ProductId;

import java.util.List;

@AggregateRoot
public class Purchase {
    private final List<ProductId> productsIds;

    public Purchase(List<ProductId> productsIds) {
        this.productsIds = productsIds;
    }
}
