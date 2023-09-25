package com.smalaca.purchase.domain.offer;

import java.util.UUID;

public record DeliveryRequest(UUID deliveryMethodId, AddressDto addressDto) {
}
