package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.selection.Selection;
import com.smalaca.purchase.domain.selection.SelectionFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RemoveProductsCommand(UUID cartId, Map<UUID, Integer> products) {
    List<Selection> selections() {
        return SelectionFactory.selections(products);
    }
}
