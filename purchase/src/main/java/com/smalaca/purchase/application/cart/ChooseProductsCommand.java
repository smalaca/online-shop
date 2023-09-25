package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.deliveryaddress.AddressDto;
import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;

import java.util.Map;
import java.util.UUID;

public record ChooseProductsCommand(
        UUID buyerId, UUID cartId, Map<UUID, Integer> products, UUID deliveryMethodId, AddressDto addressDto) {
    ChooseProductsDomainCommand asCommand() {
        return new ChooseProductsDomainCommand(buyerId, ProductsFactory.create(products), deliveryMethodId, addressDto);
    }
}
