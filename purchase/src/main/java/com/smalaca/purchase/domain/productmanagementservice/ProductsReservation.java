package com.smalaca.purchase.domain.productmanagementservice;

import java.util.List;
import java.util.UUID;

public record ProductsReservation(boolean wasReserved, List<AvailableProduct> reservedProducts, List<UUID> missingProducts) {
    public boolean wasNotReserved() {
        return !wasReserved;
    }
}
