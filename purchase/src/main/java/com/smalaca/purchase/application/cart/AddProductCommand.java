package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public record AddProductCommand(UUID cartId, Map<UUID, Integer> products) {
    List<Product> asProducts() {
        return products().entrySet().stream()
                .map(entry -> Product.product(entry.getKey(), entry.getValue()))
                .collect(toList());
    }
}
