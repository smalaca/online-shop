package com.smalaca.purchase.domain.productmanagementservice;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;

import java.util.List;
import java.util.UUID;

@SecondaryPort
public interface ProductManagementService {
    List<AvailableProduct> getAvailabilityOf(List<UUID> productIds);
}
