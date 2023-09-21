package com.smalaca.purchase.domain.cart;

import java.util.List;
import java.util.UUID;

public interface ProductManagementService {
    List<Product> getAvailabilityOf(List<UUID> productIds);
}
