package com.smalaca.purchase.domain.order;

import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

public record AcceptOfferDomainCommand(UUID offerId, Delivery delivery, List<Product> products) {
}
