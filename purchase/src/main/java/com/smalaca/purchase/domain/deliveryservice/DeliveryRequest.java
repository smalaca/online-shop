package com.smalaca.purchase.domain.deliveryservice;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;

import java.util.UUID;

public record DeliveryRequest(UUID deliveryMethodId, DeliveryAddress deliveryAddress) {
}
