package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;

import java.util.Map;
import java.util.UUID;

public record ChooseProductsCommand(UUID cartId, Map<UUID, Integer> products, UUID deliveryMethodId) {
    ChooseProductsDomainCommand asCommand() {
        return new ChooseProductsDomainCommand(ProductsFactory.create(products), deliveryMethodId);
    }
}
