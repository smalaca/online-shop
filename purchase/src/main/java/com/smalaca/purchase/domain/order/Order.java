package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.purchase.domain.productid.ProductId;

import java.util.List;

@AggregateRoot
public class Order {
    private final List<ProductId> productsIds;

    public Order(List<ProductId> productsIds) {
        this.productsIds = productsIds;
    }
}
