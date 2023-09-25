package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.productmanagementservice.AvailableProduct;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;

import java.math.BigDecimal;
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

    GivenAvailability available(UUID sellerId, UUID productId, int amount, BigDecimal price) {
        products.add(productId);
        availableProducts.add(AvailableProduct.availableProduct(sellerId, productId, amount, price));
        return this;
    }

    void set() {
        given(productManagementService.getAvailabilityOf(products)).willReturn(availableProducts);
    }
}
