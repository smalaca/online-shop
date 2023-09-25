package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RemoveProductsCommand(UUID cartId, Map<UUID, Integer> products) {
    List<Product> asProducts() {
        return ProductsFactory.create(products);
    }
}
