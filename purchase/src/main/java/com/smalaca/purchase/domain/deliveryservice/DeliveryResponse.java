package com.smalaca.purchase.domain.deliveryservice;

import com.smalaca.purchase.domain.offer.DeliveryStatusCode;
import com.smalaca.purchase.domain.price.Price;

import static com.smalaca.purchase.domain.offer.DeliveryStatusCode.NOT_EXISTING_ADDRESS;
import static com.smalaca.purchase.domain.offer.DeliveryStatusCode.UNSUPPORTED_METHOD;

public record DeliveryResponse(DeliveryStatusCode deliveryStatusCode, Price price) {
    public boolean isDeliveryMethodUnsupported() {
        return UNSUPPORTED_METHOD.equals(deliveryStatusCode);
    }

    public boolean isNotExistingAddress() {
        return NOT_EXISTING_ADDRESS.equals(deliveryStatusCode);
    }
}
