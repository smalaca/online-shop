package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

class ProductsFactory {
    static List<Product> create(Map<UUID, Integer> products) {
        return products.entrySet().stream()
                .map(entry -> Product.product(entry.getKey(), entry.getValue()))
                .collect(toList());
    }
}
