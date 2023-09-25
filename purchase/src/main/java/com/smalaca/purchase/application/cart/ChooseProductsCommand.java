package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ChooseProductsCommand(UUID cartId, Map<UUID, Integer> products, String deliveryMethod) {
    List<Product> asProducts() {
        return ProductsFactory.create(products);
    }
}
