package com.smalaca.purchase.domain.productmanagementservice;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;

import java.util.List;
import java.util.UUID;

@SecondaryPort
public interface ProductManagementService {
    List<QuantitativeProduct> getAvailabilityOf(List<UUID> productsIds);

    ProductsReservation reserve(UUID buyerId, List<QuantitativeProduct> quantitativeProducts);
}
