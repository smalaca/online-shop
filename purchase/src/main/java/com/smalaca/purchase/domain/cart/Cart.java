package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.offer.Offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AggregateRoot
public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    @PrimaryPort
    public void add(List<Product> products) {
        products.forEach(product -> {
            Optional<CartItem> found = cartItemFor(product);

            if (found.isPresent()) {
                found.get().increase(product.getAmount());
            } else {
                items.add(product.asCartItem());
            }
        });
    }

    private Optional<CartItem> cartItemFor(Product product) {
        return items.stream().filter(item -> item.isFor(product)).findFirst();
    }

    @PrimaryPort
    public void remove(List<Product> products) {
        products.forEach(product -> {
            cartItemFor(product).ifPresent(cartItem -> {
                if (cartItem.hasMoreThan(product)){
                    cartItem.decrease(product.getAmount());
                } else {
                    items.remove(cartItem);
                }
            });
        });
    }

    @PrimaryPort
    @Factory
    public Offer choose(List<Product> products) {
        if (products.isEmpty()) {
            throw new NoProductsChosenException();
        }
        return null;
    }
}
