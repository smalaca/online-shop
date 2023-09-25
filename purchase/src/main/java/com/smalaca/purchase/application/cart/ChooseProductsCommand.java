package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;
import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ChooseProductsCommand(
        UUID buyerId, UUID cartId, Map<UUID, Integer> products, UUID deliveryMethodId, AddressDto deliveryAddress) {
    ChooseProductsDomainCommand asCommand() {
        List<Product> products = ProductsFactory.create(this.products);
        DeliveryAddress deliveryAddress = this.deliveryAddress.asDeliveryAddress();
        return new ChooseProductsDomainCommand(buyerId, products, deliveryMethodId, deliveryAddress);
    }
}
