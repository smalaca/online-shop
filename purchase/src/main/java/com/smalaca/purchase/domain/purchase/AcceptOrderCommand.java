package com.smalaca.purchase.domain.purchase;

import java.util.UUID;

public record AcceptOrderCommand(UUID buyerId, UUID orderId, UUID paymentMethodId) {
}
