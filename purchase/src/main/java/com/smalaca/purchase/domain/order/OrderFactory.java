package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import com.smalaca.purchase.domain.productmanagementservice.ProductReservation;

import java.util.List;
import java.util.UUID;

@Factory
public class OrderFactory {
    private final ProductManagementService productManagementService;

    public OrderFactory(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    public Order create(AcceptOfferDomainCommand command) {
        Order.Builder builder = new Order.Builder()
                .offerId(command.offerId())
                .delivery(command.delivery());

        ProductReservation productReservation = productManagementService.reserve(command.buyerId(), command.products());

        if (productReservation.wasNotReserved()) {
            throw OrderException.notAvailableProducts(notAvailableProducts(command, productReservation));
        }

        productReservation.reservedProducts().forEach(builder::item);

        return builder.build();
    }

    private List<Product> notAvailableProducts(AcceptOfferDomainCommand command, ProductReservation productReservation) {
        List<UUID> missingProducts = productReservation.missingProducts();

        return command.products().stream()
                .filter(product -> missingProducts.contains(product.getProductId()))
                .toList();
    }
}
