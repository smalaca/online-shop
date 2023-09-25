package com.smalaca.purchase.domain.offer;

import java.util.UUID;

public interface DeliveryService {
    DeliveryPrice calculate(UUID deliveryMethodId);
}
