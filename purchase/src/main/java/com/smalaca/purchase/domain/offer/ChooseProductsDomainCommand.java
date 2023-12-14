package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.deliveryservice.DeliveryRequest;

import java.util.List;
import java.util.UUID;

public record ChooseProductsDomainCommand(UUID buyerId, List<com.smalaca.purchase.domain.selection.Selection> selections, UUID deliveryMethodId, DeliveryAddress deliveryAddress) {
    public boolean hasNothingSelected() {
        return selections.isEmpty();
    }

    DeliveryRequest asDeliveryRequest() {
        return new DeliveryRequest(deliveryMethodId, deliveryAddress);
    }
}
