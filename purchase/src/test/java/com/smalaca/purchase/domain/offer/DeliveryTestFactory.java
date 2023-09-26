package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.price.Price;

import java.util.UUID;

public class DeliveryTestFactory {
    public static Delivery delivery(UUID methodId, DeliveryAddress address, Price price) {
        return new Delivery(methodId, address, price);
    }
}