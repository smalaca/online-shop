package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferFactory;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.selection.Selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@AggregateRoot
public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    Cart() {}

    @PrimaryPort
    public void add(List<Selection> selections) {
        selections.forEach(selection -> {
            Optional<CartItem> found = cartItemFor(selection);

            if (found.isPresent()) {
                found.get().increase(selection.getQuantity());
            } else {
                items.add(asCartItem(selection));
            }
        });
    }

    private CartItem asCartItem(Selection selection) {
        return new CartItem(selection.getProductId(), selection.getQuantity());
    }

    private Optional<CartItem> cartItemFor(Selection selection) {
        return items.stream().filter(item -> item.isFor(selection)).findFirst();
    }

    @PrimaryPort
    public void remove(List<Product> products) {
        products.forEach(product -> {
            cartItemFor(product.asSelection()).ifPresent(cartItem -> {
                if (cartItem.hasMoreThan(product)){
                    cartItem.decrease(product.getQuantity());
                } else {
                    items.remove(cartItem);
                }
            });
        });
    }

    @PrimaryPort
    @Factory
    public Offer choose(ChooseProductsDomainCommand command, OfferFactory offerFactory) {
        if (command.hasNoProducts()) {
            throw CartProductsException.choseNothing();
        }

        List<Product> missing = getMissingOf(command.products());

        if (!missing.isEmpty()) {
            throw CartProductsException.missing(missing);
        }

        return offerFactory.create(command);
    }

    private List<Product> getMissingOf(List<Product> products) {
        return products.stream()
                .filter(this::isMissing)
                .collect(toList());
    }

    private boolean isMissing(Product product) {
        Optional<CartItem> existing = items.stream()
                .filter(cartItem -> cartItem.isEnoughOf(product))
                .findFirst();

        return existing.isEmpty();
    }
}
