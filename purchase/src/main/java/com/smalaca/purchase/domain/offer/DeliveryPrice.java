package com.smalaca.purchase.domain.offer;

import static com.smalaca.purchase.domain.offer.DeliveryStatusCode.UNSUPPORTED_METHOD;

public record DeliveryPrice(DeliveryStatusCode deliveryStatusCode) {
    boolean isMethodUnsupported() {
        return UNSUPPORTED_METHOD.equals(deliveryStatusCode);
    }
}
