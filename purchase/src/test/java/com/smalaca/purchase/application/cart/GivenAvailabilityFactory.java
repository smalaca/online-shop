package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.offer.ProductManagementService;

import java.util.UUID;

class GivenAvailabilityFactory {
    private final ProductManagementService productManagementService;

    GivenAvailabilityFactory(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    GivenAvailability notAvailable(UUID productId) {
        return new GivenAvailability(productManagementService).notAvailable(productId);
    }

    GivenAvailability available(UUID productId, int amount) {
        return new GivenAvailability(productManagementService).available(productId, amount);
    }
}
