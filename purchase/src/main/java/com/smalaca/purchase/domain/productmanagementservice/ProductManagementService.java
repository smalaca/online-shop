package com.smalaca.purchase.domain.productmanagementservice;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

@SecondaryPort
public interface ProductManagementService {
    List<AvailableProduct> getAvailabilityOf(List<UUID> productsIds);

    List<AvailableProduct> reserve(List<Product> products);
}
