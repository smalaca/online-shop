package com.smalaca.purchase.domain.productmanagementservice;

import com.smalaca.purchase.domain.product.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

public class GivenAvailability {
    private final ProductManagementService productManagementService;
    private final List<UUID> productsIds = new ArrayList<>();
    private final List<UUID> missingProductsIds = new ArrayList<>();
    private final List<AvailableProduct> availableProducts = new ArrayList<>();

    public GivenAvailability(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    public GivenAvailability notAvailable(UUID productId) {
        productsIds.add(productId);
        missingProductsIds.add(productId);
        return this;
    }

    public GivenAvailability available(UUID sellerId, UUID productId, int quantity, BigDecimal price) {
        productsIds.add(productId);
        availableProducts.add(AvailableProduct.availableProduct(sellerId, productId, quantity, price));
        return this;
    }

    public void forChecking() {
        given(productManagementService.getAvailabilityOf(productsIds)).willReturn(availableProducts);
    }

    public void forReserving(UUID buyerId, List<Product> products) {
        ProductsReservation response = new ProductsReservation(hasNoMissingProducts(), availableProducts, missingProductsIds);
        given(productManagementService.reserve(buyerId, products)).willReturn(response);
    }

    private boolean hasNoMissingProducts() {
        return missingProductsIds.isEmpty();
    }
}
