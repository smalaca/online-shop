package com.smalaca.purchase.domain.offer;

import java.util.List;
import java.util.UUID;

public interface ProductManagementService {
    List<AvailableProduct> getAvailabilityOf(List<UUID> productIds);
}
