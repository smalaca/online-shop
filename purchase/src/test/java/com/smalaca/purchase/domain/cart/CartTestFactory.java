package com.smalaca.purchase.domain.cart;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;

public class CartTestFactory {
    public static Cart cart(List<Product> products) {
        Cart cart = new Cart();
        cart.add(products);
        return cart;
    }
}
