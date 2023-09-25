package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.offer.AvailableProduct;
import com.smalaca.purchase.domain.offer.ProductManagementService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

class GivenAvailability {
    private final ProductManagementService productManagementService;
    private final List<UUID> products = new ArrayList<>();
    private final List<AvailableProduct> availableProducts = new ArrayList<>();

    GivenAvailability(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    GivenAvailability notAvailable(UUID productId) {
        products.add(productId);
        return this;
    }

    GivenAvailability available(UUID productId, int amount) {
        products.add(productId);
        availableProducts.add(AvailableProduct.product(productId, amount));
        return this;
    }

    void set() {
        given(productManagementService.getAvailabilityOf(products)).willReturn(availableProducts);
    }
}
