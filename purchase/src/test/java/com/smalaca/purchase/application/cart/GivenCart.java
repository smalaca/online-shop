package com.smalaca.purchase.application.cart;

import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;
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

    GivenCart withProduct(UUID productId, int amount) {
        products.add(Product.product(productId, amount));
        return this;
    }

    void with(CartId cartId) {
        Cart cart = new Cart();
        cart.add(products);
        given(cartRepository.findBy(cartId)).willReturn(cart);
    }
}
