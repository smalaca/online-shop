package com.smalaca.purchase.domain.offer;

public interface DeliveryService {
    DeliveryResponse calculate(DeliveryRequest deliveryRequest);
}
