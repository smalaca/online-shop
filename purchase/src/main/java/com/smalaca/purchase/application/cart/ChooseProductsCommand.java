package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;
import com.smalaca.purchase.domain.selection.Selection;
import com.smalaca.purchase.domain.selection.SelectionFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ChooseProductsCommand(
        UUID buyerId, UUID cartId, Map<UUID, Integer> products, UUID deliveryMethodId, AddressDto addressDto) {
    ChooseProductsDomainCommand asCommand() {
        List<Selection> selections = SelectionFactory.selections(this.products);
        DeliveryAddress deliveryAddress = this.addressDto.asDeliveryAddress();
        return new ChooseProductsDomainCommand(buyerId, selections, deliveryMethodId, deliveryAddress);
    }
}
