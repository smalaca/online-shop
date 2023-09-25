package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

public record ChooseProductsDomainCommand(List<Product> products, UUID deliveryMethodId) {
    public boolean hasNoProducts() {
        return products.isEmpty();
    }
}