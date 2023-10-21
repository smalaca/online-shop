package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.cart.CartRepository;

import java.util.UUID;

class GivenCartFactory {
    private final CartRepository cartRepository;

    GivenCartFactory(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    GivenCart withProduct(UUID productId, int quantity) {
        return givenCart().withProduct(productId, quantity);
    }

    void empty(UUID cartId) {
        givenCart().with(cartId);
    }

    private GivenCart givenCart() {
        return new GivenCart(cartRepository);
    }
}
