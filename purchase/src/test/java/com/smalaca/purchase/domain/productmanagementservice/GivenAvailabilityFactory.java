package com.smalaca.purchase.domain.productmanagementservice;

import java.math.BigDecimal;
import java.util.UUID;

public class GivenAvailabilityFactory {
    private final ProductManagementService productManagementService;

    public GivenAvailabilityFactory(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    public GivenAvailability notAvailable(UUID productId) {
        return new GivenAvailability(productManagementService).notAvailable(productId);
    }

    public GivenAvailability available(UUID sellerId, UUID productId, int amount, BigDecimal price) {
        return new GivenAvailability(productManagementService).available(sellerId, productId, amount, price);
    }
}
