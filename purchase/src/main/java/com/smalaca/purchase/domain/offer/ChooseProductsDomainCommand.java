package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

public record ChooseProductsDomainCommand(UUID buyerId, List<Product> products, UUID deliveryMethodId, AddressDto addressDto) {
    public boolean hasNoProducts() {
        return products.isEmpty();
    }

    DeliveryRequest asDeliveryRequest() {
        return new DeliveryRequest(deliveryMethodId, addressDto);
    }
}
