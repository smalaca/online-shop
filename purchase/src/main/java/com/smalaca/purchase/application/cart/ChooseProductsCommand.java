package com.smalaca.purchase.application.cart;

import java.util.List;
import java.util.UUID;

public record ChooseProductsCommand(UUID cartId, List<UUID> productsIds) {
}
