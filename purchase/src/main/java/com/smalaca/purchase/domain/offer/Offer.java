package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.order.Order;

import java.time.LocalDateTime;

@AggregateRoot
public class Offer {
    private final LocalDateTime creationDateTime;

    Offer(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    @PrimaryPort
    public void reject() {

    }

    @PrimaryPort
    @Factory
    public Offer recreate() {
        return new Offer(null);
    }

    @PrimaryPort
    @Factory
    public Order accept() {
        return new Order();
    }
}
