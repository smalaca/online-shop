package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.productid.ProductId;

import java.util.List;

@AggregateRoot
public class Offer {
    private final List<ProductId> productsIds;

    public Offer(List<ProductId> productsIds) {
        this.productsIds = productsIds;
    }

    @PrimaryPort
    public void reject() {

    }

    @PrimaryPort
    @Factory
    public Offer recreate() {
        return new Offer(productsIds);
    }

    @PrimaryPort
    @Factory
    public Order accept() {
        return new Order(productsIds);
    }
}
