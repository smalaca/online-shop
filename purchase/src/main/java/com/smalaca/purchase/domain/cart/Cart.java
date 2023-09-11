package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;

import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public class Cart {
    private final List<ProductId> products = new ArrayList<>();

    @PrimaryPort
    public void addProduct(ProductId productId) {
        products.add(productId);
    }
}
