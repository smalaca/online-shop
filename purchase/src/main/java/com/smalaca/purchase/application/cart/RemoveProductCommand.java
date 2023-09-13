package com.smalaca.purchase.application.cart;

import java.util.UUID;

public record RemoveProductCommand(UUID cartId, UUID productId) {
}
