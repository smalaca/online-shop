package com.smalaca.purchase.domain.deliveryservice;

import com.smalaca.purchase.domain.deliveryaddress.AddressDto;

import java.util.UUID;

public record DeliveryRequest(UUID deliveryMethodId, AddressDto addressDto) {
}
