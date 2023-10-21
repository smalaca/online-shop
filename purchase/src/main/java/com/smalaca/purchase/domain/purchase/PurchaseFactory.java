package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.clock.Clock;

@Factory
public class PurchaseFactory {
    private final Clock clock;

    public PurchaseFactory(Clock clock) {
        this.clock = clock;
    }

    public Purchase create(AcceptOrderCommand command) {
        return new Purchase.Builder()
                .buyerId(command.buyerId())
                .orderId(command.orderId())
                .creationDateTime(clock.nowDateTime())
                .paymentMethodId(command.paymentMethodId())
                .build();
    }
}
