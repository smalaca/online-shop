package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.product.Product;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Factory
public class OfferFactory {
    private final ProductManagementService productManagementService;
    private final DeliveryService deliveryService;
    private final Clock clock;

    public OfferFactory(ProductManagementService productManagementService, DeliveryService deliveryService, Clock clock) {
        this.productManagementService = productManagementService;
        this.deliveryService = deliveryService;
        this.clock = clock;
    }

    public Offer create(ChooseProductsDomainCommand command) {
        DeliveryResponse deliveryResponse = deliveryService.calculate(command.asDeliveryRequest());

        if (deliveryResponse.isMethodUnsupported()) {
            throw OfferException.unsupportedDeliveryMethod(command.deliveryMethodId());
        }

        if (deliveryResponse.isNotExistingAddress()) {
            throw OfferException.notExistingAddress(command.addressDto());
        }

        List<AvailableProduct> availableProducts = availableProductsFor(command.products());
        List<Product> notAvailable = notAvailableOf(command.products(), availableProducts);

        if (!notAvailable.isEmpty()) {
            throw OfferException.notAvailableProducts(notAvailable);
        }

        Offer.Builder builder = new Offer.Builder();
        command.products().forEach(product -> {
            AvailableProduct availableProduct = availableProductFor(product.getProductId(), availableProducts);
            builder.item(availableProduct.getSellerId(), product.getProductId(), product.getAmount(), availableProduct.getPrice());
        });

        return builder
                .creationDateTime(clock.nowDateTime())
                .deliveryMethodId(command.deliveryMethodId())
                .build();
    }

    private AvailableProduct availableProductFor(UUID productId, List<AvailableProduct> availableProducts) {
        return availableProducts
                .stream()
                .filter(availableProduct -> availableProduct.isFor(productId))
                .findFirst()
                .get();
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
