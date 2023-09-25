package com.smalaca.purchase.domain.deliveryservice;

public interface DeliveryService {
    DeliveryResponse calculate(DeliveryRequest deliveryRequest);
}
