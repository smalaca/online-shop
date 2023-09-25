package com.smalaca.purchase.domain.offer;

public interface DeliveryService {
    DeliveryPlan calculate(String deliveryMethod);
}
