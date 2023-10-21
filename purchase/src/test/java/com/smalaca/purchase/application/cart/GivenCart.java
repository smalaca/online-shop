package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.cart.CartTestFactory;
import com.smalaca.purchase.domain.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

class GivenCart {
    private final CartRepository cartRepository;
    private final List<Product> products = new ArrayList<>();

    GivenCart(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    GivenCart withProduct(UUID productId, int quantity) {
        products.add(Product.product(productId, quantity));
        return this;
    }

    void with(UUID cartId) {
        Cart cart = CartTestFactory.cart(products);
        given(cartRepository.findBy(cartId)).willReturn(cart);
    }
}
