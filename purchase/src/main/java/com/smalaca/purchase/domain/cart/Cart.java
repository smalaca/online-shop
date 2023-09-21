package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.offer.Offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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
    public Offer choose(List<Product> products, ProductManagementService productManagementService) {
        if (products.isEmpty()) {
            throw CartProductsException.choseNothing();
        }

        List<Product> missing = getMissingOf(products);

        if (!missing.isEmpty()) {
            throw CartProductsException.missing(missing);
        }

        List<Product> notAvailable = getNotAvailableOf(products, productManagementService);

        if (!notAvailable.isEmpty()) {
            throw CartProductsException.notAvailable(notAvailable);
        }

        return null;
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

    private List<Product> getNotAvailableOf(List<Product> products, ProductManagementService productManagementService) {
        List<UUID> productIds = products.stream().map(Product::getProductId).collect(toList());
        List<Product> available = productManagementService.getAvailabilityOf(productIds);

        return products.stream()
                .filter(product -> isAvailabilityNotSatisfied(available, product))
                .collect(toList());
    }

    private boolean isAvailabilityNotSatisfied(List<Product> products, Product product) {
        return products.stream().noneMatch(product::isAvailabilitySatisfied);
    }
}
