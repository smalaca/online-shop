package com.smalaca.purchase.domain.offer;

import java.util.UUID;

public interface DeliveryService {
    DeliveryPlan calculate(UUID deliveryMethodId);
}
