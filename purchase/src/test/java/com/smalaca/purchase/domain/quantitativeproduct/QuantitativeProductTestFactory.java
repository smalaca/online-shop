package com.smalaca.purchase.domain.quantitativeproduct;

import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.quantity.Quantity;

import java.math.BigDecimal;
import java.util.UUID;

public class QuantitativeProductTestFactory {
    public static QuantitativeProduct quantitativeProduct(UUID sellerId, UUID productId, int quantity, BigDecimal price) {
        return new QuantitativeProduct(sellerId, productId, Quantity.quantity(quantity), Price.price(price));
    }
}