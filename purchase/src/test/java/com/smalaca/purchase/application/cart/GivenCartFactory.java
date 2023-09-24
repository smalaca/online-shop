package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;

import java.util.UUID;

class GivenCartFactory {
    private final CartRepository cartRepository;

    GivenCartFactory(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    GivenCart withProduct(UUID productId, int amount) {
        return givenCart().withProduct(productId, amount);
    }

    void empty(CartId cartId) {
        givenCart().with(cartId);
    }

    private GivenCart givenCart() {
        return new GivenCart(cartRepository);
    }
}
