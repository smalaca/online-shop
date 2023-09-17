package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.productid.ProductId;

import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public class Cart {
    private final BuyerId buyerId;

    private final List<ProductId> products = new ArrayList<>();

    Cart(BuyerId buyerId) {
        this.buyerId = buyerId;
    }

    @PrimaryPort
    public void addProduct(ProductId productId) {
        products.add(productId);
    }

    @PrimaryPort
    public void removeProduct(ProductId productId) {
        products.remove(productId);
    }

    @PrimaryPort
    @Factory
    public Offer choose(List<ProductId> productsIds) {
        return new Offer(productsIds);
    }
}
