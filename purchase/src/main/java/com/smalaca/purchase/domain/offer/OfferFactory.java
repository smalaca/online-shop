package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.deliveryservice.DeliveryResponse;
import com.smalaca.purchase.domain.deliveryservice.DeliveryService;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;
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

        List<QuantitativeProduct> availableProducts = availableProductsFor(command.selections());
        List<Selection> notAvailable = notAvailableOf(command.selections(), availableProducts);

        if (!notAvailable.isEmpty()) {
            throw OfferException.notAvailableProducts(notAvailable);
        }

        Offer.Builder builder = new Offer.Builder();
        command.selections().forEach(selection -> {
            QuantitativeProduct availableProduct = availableProductFor(selection.getProductId(), availableProducts);
            builder.item(availableProduct, selection.getQuantity());
        });

        return builder
                .buyerId(command.buyerId())
                .creationDateTime(clock.nowDateTime())
                .delivery(command.deliveryMethodId(), command.deliveryAddress(), deliveryResponse.price())
                .build();
    }

    private QuantitativeProduct availableProductFor(UUID productId, List<QuantitativeProduct> availableProducts) {
        return availableProducts
                .stream()
                .filter(availableProduct -> availableProduct.isFor(productId))
                .findFirst()
                .get();
    }

    private List<QuantitativeProduct> availableProductsFor(List<Selection> selections) {
        List<UUID> productIds = selections.stream().map(Selection::getProductId).collect(toList());
        return productManagementService.getAvailabilityOf(productIds);
    }

    private List<Selection> notAvailableOf(List<Selection> selections, List<QuantitativeProduct> available) {
        return selections.stream()
                .filter(selection -> isAvailabilityNotSatisfied(selection, available))
                .collect(toList());
    }

    private boolean isAvailabilityNotSatisfied(Selection selection, List<QuantitativeProduct> products) {
        return products.stream().noneMatch(available -> available.isAvailableFor(selection));
    }
}
