package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;

import java.util.List;
import java.util.UUID;

public record AcceptOfferDomainCommand(UUID buyerId, UUID offerId, Delivery delivery, List<QuantitativeProduct> quantitativeProducts) {
}
