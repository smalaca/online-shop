package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.deliveryservice.DeliveryResponse;
import com.smalaca.purchase.domain.deliveryservice.DeliveryService;
import com.smalaca.purchase.domain.productmanagementservice.AvailableProduct;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import com.smalaca.purchase.domain.selection.Selection;

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

        if (deliveryResponse.isDeliveryMethodUnsupported()) {
            throw OfferException.unsupportedDeliveryMethod(command.deliveryMethodId());
        }

        if (deliveryResponse.isNotExistingAddress()) {
            throw OfferException.notExistingAddress(command.deliveryAddress());
        }

        List<AvailableProduct> availableProducts = availableProductsFor(command.selections());
        List<Selection> notAvailable = notAvailableOf(command.selections(), availableProducts);

        if (!notAvailable.isEmpty()) {
            throw OfferException.notAvailableProducts(notAvailable);
        }

        Offer.Builder builder = new Offer.Builder();
        command.selections().forEach(product -> {
            AvailableProduct availableProduct = availableProductFor(product.getProductId(), availableProducts);
            builder.item(availableProduct.getSellerId(), product.getProductId(), product.getQuantity(), availableProduct.getPrice());
        });

        return builder
                .buyerId(command.buyerId())
                .creationDateTime(clock.nowDateTime())
                .delivery(command.deliveryMethodId(), command.deliveryAddress(), deliveryResponse.price())
                .build();
    }

    private AvailableProduct availableProductFor(UUID productId, List<AvailableProduct> availableProducts) {
        return availableProducts
                .stream()
                .filter(availableProduct -> availableProduct.isFor(productId))
                .findFirst()
                .get();
    }

    private List<AvailableProduct> availableProductsFor(List<Selection> selections) {
        List<UUID> productIds = selections.stream().map(Selection::getProductId).collect(toList());
        return productManagementService.getAvailabilityOf(productIds);
    }

    private List<Selection> notAvailableOf(List<Selection> selections, List<AvailableProduct> available) {
        return selections.stream()
                .filter(selection -> isAvailabilityNotSatisfied(available, selection))
                .collect(toList());
    }

    private boolean isAvailabilityNotSatisfied(List<AvailableProduct> products, Selection selection) {
        return products.stream().noneMatch(available -> available.isAvailableFor(selection));
    }
}
