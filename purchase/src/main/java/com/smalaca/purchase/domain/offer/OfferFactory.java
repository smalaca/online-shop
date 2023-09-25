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
        List<AvailableProduct> availableProducts = availableProductsFor(products);
        List<Product> notAvailable = notAvailableOf(products, availableProducts);

        if (!notAvailable.isEmpty()) {
            throw OfferProductsException.notAvailable(notAvailable);
        }

        Offer.Builder builder = new Offer.Builder();
        products.forEach(product -> {
            builder.item(product.getProductId(), product.getAmount());
        });

        return builder
                .creationDateTime(clock.nowDateTime())
                .build();
    }

    private List<AvailableProduct> availableProductsFor(List<Product> products) {
        List<UUID> productIds = products.stream().map(Product::getProductId).collect(toList());
        return productManagementService.getAvailabilityOf(productIds);
    }

    private List<Product> notAvailableOf(List<Product> products, List<AvailableProduct> available) {
        return products.stream()
                .filter(product -> isAvailabilityNotSatisfied(available, product))
                .collect(toList());
    }

    private boolean isAvailabilityNotSatisfied(List<AvailableProduct> products, Product product) {
        return products.stream().noneMatch(available -> available.isAvailableFor(product));
    }
}
