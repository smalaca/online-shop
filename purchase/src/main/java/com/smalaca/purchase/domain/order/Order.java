package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.purchase.Purchase;

@AggregateRoot
public class Order {
    @PrimaryPort
    public void reject() {

    }

    @PrimaryPort
    @Factory
    public Purchase purchase() {
        return new Purchase();
    }
}
