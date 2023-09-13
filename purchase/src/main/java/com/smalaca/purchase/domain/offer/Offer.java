package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
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

    @PrimaryAdapter
    @Factory
    public Offer recreate() {
        return new Offer(productsIds);
    }
}
