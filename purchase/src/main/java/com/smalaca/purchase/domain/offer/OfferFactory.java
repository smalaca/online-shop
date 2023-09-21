package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Factory
public class OfferFactory {
    private final ProductManagementService productManagementService;
    private final Clock clock;

    public OfferFactory(ProductManagementService productManagementService, Clock clock) {
        this.productManagementService = productManagementService;
        this.clock = clock;
    }

    public Offer create(List<Product> products) {
        List<Product> notAvailable = getNotAvailableOf(products);

        if (!notAvailable.isEmpty()) {
            throw OfferProductsException.notAvailable(notAvailable);
        }

        return new Offer(clock.nowDateTime());
    }

    private List<Product> getNotAvailableOf(List<Product> products) {
        List<UUID> productIds = products.stream().map(Product::getProductId).collect(toList());
        List<Product> available = productManagementService.getAvailabilityOf(productIds);

        return products.stream()
                .filter(product -> isAvailabilityNotSatisfied(available, product))
                .collect(toList());
    }

    private boolean isAvailabilityNotSatisfied(List<Product> products, Product product) {
        return products.stream().noneMatch(product::isAvailabilitySatisfied);
    }
}
