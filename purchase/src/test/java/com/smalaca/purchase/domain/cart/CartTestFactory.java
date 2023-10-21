package com.smalaca.purchase.domain.cart;

import com.smalaca.purchase.domain.selection.Selection;

import java.util.List;

public class CartTestFactory {
    public static Cart cart(List<Selection> selections) {
        Cart cart = new Cart();
        cart.add(selections);
        return cart;
    }
}
