package com.smalaca.purchase.domain.deliveryservice;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;

import java.util.UUID;

public class GivenDeliveryFactory {
    private final DeliveryService deliveryService;

    public GivenDeliveryFactory(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public GivenDelivery forRequest(UUID deliveryMethodId, DeliveryAddress deliveryAddress) {
        return new GivenDelivery(deliveryService, deliveryMethodId, deliveryAddress);
    }
}
