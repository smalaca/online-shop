package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.deliveryaddress.AddressDto;

import java.util.UUID;

public record DeliveryRequest(UUID deliveryMethodId, AddressDto addressDto) {
}
