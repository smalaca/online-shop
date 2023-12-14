package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;

import java.util.List;

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
                .totalPrice(totalPrice(command.deliveryCost(), command.quantitativeProducts()))
                .build();
    }

    private Price totalPrice(Price deliveryCost, List<QuantitativeProduct> products) {
        return deliveryCost.add(priceFor(products));
    }

    private Price priceFor(List<QuantitativeProduct> products) {
        return products.stream()
                .map(product -> product.getPrice().multiply(product.getQuantity()))
                .reduce(Price.ZERO, Price::add);
    }
}
