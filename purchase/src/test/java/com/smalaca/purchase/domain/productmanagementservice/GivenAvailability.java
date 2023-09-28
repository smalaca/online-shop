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
    private final List<Product> products = new ArrayList<>();
    private final List<AvailableProduct> availableProducts = new ArrayList<>();

    public GivenAvailability(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    GivenAvailability notAvailable(UUID productId) {
        productsIds.add(productId);
        return this;
    }

    public GivenAvailability available(UUID sellerId, UUID productId, int amount, BigDecimal price) {
        productsIds.add(productId);
        products.add(Product.product(productId, amount));
        availableProducts.add(AvailableProduct.availableProduct(sellerId, productId, amount, price));
        return this;
    }

    public void forChecking() {
        given(productManagementService.getAvailabilityOf(productsIds)).willReturn(availableProducts);
    }

    public void forReservingTo(UUID buyerId) {
        given(productManagementService.reserve(buyerId, products)).willReturn(availableProducts);
    }
}
