package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;

public record ChooseProductsDomainCommand(List<Product> products, String deliveryMethod) {
    public boolean hasNoProducts() {
        return products.isEmpty();
    }
}
