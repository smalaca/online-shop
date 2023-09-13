package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.productid.ProductId;
import com.smalaca.purchase.domain.purchase.Purchase;

import java.util.List;

@AggregateRoot
public class Order {
    private final List<ProductId> productsIds;

    public Order(List<ProductId> productsIds) {
        this.productsIds = productsIds;
    }

    @PrimaryPort
    public void reject() {

    }

    @PrimaryPort
    @Factory
    public Purchase purchase() {
        return new Purchase(productsIds);
    }
}
