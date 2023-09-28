package com.smalaca.purchase.domain.productmanagementservice;

import com.smalaca.purchase.domain.amount.Amount;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.product.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class AvailableProduct {
    private final UUID sellerId;
    private final UUID productId;
    private final Amount amount;
    private final Price price;

    private AvailableProduct(UUID sellerId, UUID productId, Amount amount, Price price) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.amount = amount;
        this.price = price;
    }

    public static AvailableProduct availableProduct(UUID sellerId, UUID productId, Integer amount, BigDecimal price) {
        return new AvailableProduct(sellerId, productId, Amount.amount(amount), Price.price(price));
    }

    public boolean isAvailableFor(Product product) {
        return product.hasProductIdSameAs(productId) && product.hasLessOrEqualThan(amount);
    }

    public boolean isFor(UUID productId) {
        return this.productId == productId;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public Amount getAmount() {
        return amount;
    }

    public Price getPrice() {
        return price;
    }
}
