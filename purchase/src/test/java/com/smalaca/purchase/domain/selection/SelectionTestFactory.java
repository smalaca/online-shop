package com.smalaca.purchase.domain.selection;

import java.util.UUID;

public class SelectionTestFactory {

    public static Selection selection(UUID productId, int quantity) {
        return Selection.selection(productId, quantity);
    }
}