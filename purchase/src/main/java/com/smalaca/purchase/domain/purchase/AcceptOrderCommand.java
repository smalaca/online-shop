package com.smalaca.purchase.domain.purchase;

import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;

import java.util.List;
import java.util.UUID;

public record AcceptOrderCommand(
        UUID buyerId, UUID orderId, UUID paymentMethodId, Price deliveryCost, List<QuantitativeProduct> quantitativeProducts) {
}
