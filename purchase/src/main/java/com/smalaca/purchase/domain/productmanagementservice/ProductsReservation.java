package com.smalaca.purchase.domain.productmanagementservice;

import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;

import java.util.List;
import java.util.UUID;

public record ProductsReservation(boolean wasReserved, List<QuantitativeProduct> reservedProducts, List<UUID> missingProducts) {
    public boolean wasNotReserved() {
        return !wasReserved;
    }
}
