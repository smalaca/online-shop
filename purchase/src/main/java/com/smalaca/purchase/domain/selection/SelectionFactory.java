package com.smalaca.purchase.domain.selection;

import com.smalaca.annotations.ddd.Factory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Factory
public class SelectionFactory {
    public static List<Selection> selections(Map<UUID, Integer> products) {
        return products.entrySet().stream()
                .map(entry -> Selection.selection(entry.getKey(), entry.getValue()))
                .collect(toList());
    }

    public static Selection selection(UUID productId, int quantity) {
        return Selection.selection(productId, quantity);
    }
}
