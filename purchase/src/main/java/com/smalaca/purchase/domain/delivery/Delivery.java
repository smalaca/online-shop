package com.smalaca.purchase.domain.delivery;

import com.smalaca.annotations.ddd.ValueObject;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.price.Price;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@ValueObject
@EqualsAndHashCode
public class Delivery {
    private final UUID methodId;
    private final DeliveryAddress address;
    private final Price price;

    public Delivery(UUID methodId, DeliveryAddress address, Price price) {
        this.methodId = methodId;
        this.address = address;
        this.price = price;
    }

    public Price getPrice() {
        return price;
    }
}
