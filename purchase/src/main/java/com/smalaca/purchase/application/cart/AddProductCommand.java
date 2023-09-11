package com.smalaca.purchase.application.cart;

import java.util.UUID;

public record AddProductCommand(UUID cartId, UUID productId) {
}
