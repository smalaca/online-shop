package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferFactory;
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
    public void remove(List<Selection> selections) {
        selections.forEach(selection -> {
            cartItemFor(selection).ifPresent(cartItem -> {
                if (cartItem.hasMoreThan(selection)){
                    cartItem.decrease(selection.getQuantity());
                } else {
                    items.remove(cartItem);
                }
            });
        });
    }

    @PrimaryPort
    @Factory
    public Offer choose(ChooseProductsDomainCommand command, OfferFactory offerFactory) {
        if (command.hasNothingSelected()) {
            throw CartProductsException.choseNothing();
        }

        List<Selection> missing = getMissingOf(command.selections());

        if (!missing.isEmpty()) {
            throw CartProductsException.missing(missing);
        }

        return offerFactory.create(command);
    }

    private List<Selection> getMissingOf(List<Selection> selections) {
        return selections.stream()
                .filter(this::isMissing)
                .collect(toList());
    }

    private boolean isMissing(Selection selection) {
        Optional<CartItem> existing = items.stream()
                .filter(cartItem -> cartItem.isEnoughOf(selection))
                .findFirst();

        return existing.isEmpty();
    }
}
