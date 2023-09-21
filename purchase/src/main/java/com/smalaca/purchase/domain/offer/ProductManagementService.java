package com.smalaca.purchase.domain.offer;

import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

public interface ProductManagementService {
    List<Product> getAvailabilityOf(List<UUID> productIds);
}
